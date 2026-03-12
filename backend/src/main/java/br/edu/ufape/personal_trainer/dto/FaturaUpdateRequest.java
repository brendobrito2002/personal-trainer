package br.edu.ufape.personal_trainer.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public record FaturaUpdateRequest(
    @Positive(message = "Valor deve ser maior que zero")
    Double valor,

    @FutureOrPresent(message = "Vencimento deve ser hoje ou no futuro")
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataVencimento
) {}