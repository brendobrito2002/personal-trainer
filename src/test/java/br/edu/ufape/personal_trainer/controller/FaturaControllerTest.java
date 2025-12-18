package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Fatura;
import br.edu.ufape.personal_trainer.service.FaturaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FaturaController.class)
class FaturaControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private FaturaService faturaService;

    @Test
    @WithMockUser(username = "joao@email.com", roles = "ALUNO")
    void devePagarFaturaComSucesso() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setUsuarioId(1L);

        Fatura faturaPaga = new Fatura();
        faturaPaga.setFaturaId(1L);
        faturaPaga.setStatus("PAGA");
        faturaPaga.setDataPagamento(LocalDate.now());
        faturaPaga.setAluno(aluno);

        when(faturaService.pagarFatura(1L)).thenReturn(faturaPaga);

        mockMvc.perform(patch("/api/faturas/1/pagar")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAGA"));
    }

    @Test
    void deveRetornar401SemAutenticacao() throws Exception {
        mockMvc.perform(patch("/api/faturas/1/pagar")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}