package br.edu.ufape.personal_trainer.service;

import br.edu.ufape.personal_trainer.dto.PlanoDeTreinoRequest;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.model.PlanoDeTreino;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;
import br.edu.ufape.personal_trainer.repository.PlanoDeTreinoRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanoDeTreinoServiceTest {

    @Mock private PlanoDeTreinoRepository planoRepo;
    @Mock private AlunoRepository alunoRepo;
    @Mock private PersonalRepository personalRepo;
    @Mock private UsuarioRepository usuarioRepo;

    @InjectMocks private PlanoDeTreinoService planoService;

    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    private Personal personalLogado;

    @BeforeEach
    void setupSecurityContext() {
        personalLogado = new Personal();
        personalLogado.setUsuarioId(10L);
        personalLogado.setEmail("prof@email.com");
        personalLogado.setRole(Role.PERSONAL);

        when(authentication.getName()).thenReturn("prof@email.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(usuarioRepo.findByEmail("prof@email.com"))
                .thenReturn(Optional.of(personalLogado));

        when(personalRepo.findById(10L))
                .thenReturn(Optional.of(personalLogado));
    }

    @Test
    @DisplayName("Não permite dois planos ativos para o mesmo aluno")
    void naoPermiteDoisPlanosAtivos() {
        Aluno aluno = new Aluno();
        aluno.setUsuarioId(1L);
        aluno.setPersonal(personalLogado);

        when(alunoRepo.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(planoRepo.findByAluno_UsuarioIdAndAtivoTrue(1L))
                .thenReturn(Optional.of(new PlanoDeTreino()));

        PlanoDeTreinoRequest request =
                new PlanoDeTreinoRequest(1L, "Novo Plano", 8, LocalDate.now());

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> planoService.criar(request)
        );

        assertEquals("Aluno já possui um plano ativo", ex.getMessage());
        verify(planoRepo, never()).save(any());
    }

    @Test
    @DisplayName("Só permite criar plano para aluno do próprio personal")
    void soPermiteCriarParaAlunoProprio() {
        Personal outroPersonal = new Personal();
        outroPersonal.setUsuarioId(99L);

        Aluno aluno = new Aluno();
        aluno.setUsuarioId(1L);
        aluno.setPersonal(outroPersonal);

        when(alunoRepo.findById(1L))
                .thenReturn(Optional.of(aluno));

        PlanoDeTreinoRequest request =
                new PlanoDeTreinoRequest(1L, "Plano", 4, LocalDate.now());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> planoService.criar(request)
        );

        assertTrue(ex.getMessage().contains("seus próprios alunos"));
    }
}
