package br.edu.ufape.personal_trainer.service;

import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock private AlunoRepository alunoRepository;
    @Mock private PersonalRepository personalRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private AlunoService alunoService;

    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    @BeforeEach
    void setupSecurityContext() {
        Personal personalLogado = new Personal();
        personalLogado.setUsuarioId(10L);
        personalLogado.setEmail("profmarcos@email.com");
        personalLogado.setRole(Role.PERSONAL);

        when(authentication.getName()).thenReturn("profmarcos@email.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(usuarioRepository.findByEmail("profmarcos@email.com")).thenReturn(Optional.of(personalLogado));
    }

    @Test
    @DisplayName("Personal só pode vincular aluno a si mesmo")
    void personalSoVinculaASiMesmo() {
        Aluno aluno = new Aluno();
        aluno.setUsuarioId(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> alunoService.VincularPersonal(1L, 99L));

        assertEquals("Você só pode vincular alunos a si mesmo", ex.getMessage());
    }

    @Test
    @DisplayName("Aluno fica ativo após vinculação correta")
    void alunoFicaAtivoAposVinculacao() {
        Aluno aluno = new Aluno();
        aluno.setUsuarioId(1L);
        aluno.setAtivo(false);

        Personal personalLogado = new Personal();
        personalLogado.setUsuarioId(10L);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(personalRepository.findById(10L)).thenReturn(Optional.of(personalLogado));
        when(alunoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        alunoService.VincularPersonal(1L, 10L);

        assertTrue(aluno.getAtivo());
        assertEquals(personalLogado, aluno.getPersonal());
        verify(alunoRepository).save(aluno);
    }
}