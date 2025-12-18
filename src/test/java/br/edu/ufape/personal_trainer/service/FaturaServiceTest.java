package br.edu.ufape.personal_trainer.service;

import br.edu.ufape.personal_trainer.dto.FaturaRequest;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Fatura;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
import br.edu.ufape.personal_trainer.repository.FaturaRepository;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;
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
class FaturaServiceTest {

    @Mock private FaturaRepository faturaRepository;
    @Mock private AlunoRepository alunoRepository;
    @Mock private PersonalRepository personalRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private FaturaService faturaService;

    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    private Personal personalLogado;

    @BeforeEach
    void setupSecurityContext() {
        personalLogado = new Personal();
        personalLogado.setUsuarioId(10L);
        personalLogado.setEmail("profmarcos@email.com");
        personalLogado.setRole(Role.PERSONAL);

        lenient().when(authentication.getName())
                .thenReturn("profmarcos@email.com");

        lenient().when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        lenient().when(usuarioRepository.findByEmail("profmarcos@email.com"))
                .thenReturn(Optional.of(personalLogado));

        lenient().when(personalRepository.findById(10L))
                .thenReturn(Optional.of(personalLogado));
    }

    @Test
    @DisplayName("Não permite duas faturas PENDENTE para o mesmo aluno")
    void naoPermiteDuasFaturasPendentes() {
        Aluno aluno = new Aluno();
        aluno.setUsuarioId(1L);
        aluno.setPersonal(personalLogado);

        when(alunoRepository.findById(1L))
                .thenReturn(Optional.of(aluno));

        when(faturaRepository.findByAluno_UsuarioIdAndStatus(1L, "PENDENTE"))
                .thenReturn(Optional.of(new Fatura()));

        FaturaRequest request =
                new FaturaRequest(1L, 200.0, LocalDate.now().plusDays(10), "PENDENTE");

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> faturaService.criar(request)
        );

        assertEquals("Aluno já possui uma fatura pendente", ex.getMessage());
        verify(faturaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Permite pagar fatura PENDENTE")
    void permitePagarFaturaPendente() {
        Fatura fatura = new Fatura();
        fatura.setFaturaId(1L);
        fatura.setStatus("PENDENTE");
        fatura.setAluno(new Aluno() {{ setUsuarioId(1L); }});

        when(faturaRepository.findById(1L))
                .thenReturn(Optional.of(fatura));

        when(faturaRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Fatura paga = faturaService.pagarFatura(1L);

        assertEquals("PAGA", paga.getStatus());
        assertNotNull(paga.getDataPagamento());
    }

    @Test
    @DisplayName("Marca automaticamente como VENCIDA se passou do vencimento")
    void marcaComoVencidaAutomaticamente() {
        Aluno aluno = new Aluno();
        aluno.setUsuarioId(1L);
        aluno.setPersonal(personalLogado);

        Fatura fatura = new Fatura();
        fatura.setFaturaId(1L);
        fatura.setStatus("PENDENTE");
        fatura.setDataVencimento(LocalDate.now().minusDays(1));
        fatura.setAluno(aluno);

        when(faturaRepository.findById(1L))
                .thenReturn(Optional.of(fatura));

        Fatura resultado = faturaService.buscarId(1L);

        assertEquals("VENCIDA", resultado.getStatus());
    }
}
