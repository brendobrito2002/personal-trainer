package br.edu.ufape.personal_trainer.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PlanoDeTreinoUpdateRequest(
    @Size(min = 1, max = 100, message = "Nome deve ter entre 1 e 100 caracteres")
    String nome,

    @PastOrPresent(message = "Data de início deve ser hoje ou no passado")
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataInicio,

    @Future(message = "Data de fim deve ser no futuro")
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataFim
) {
    @AssertTrue(message = "Data de início deve ser anterior ou igual à data de fim")
    private boolean isDataValida() {
        return dataInicio == null || dataFim == null || !dataInicio.isAfter(dataFim);
    }
}