package br.edu.ufape.personal_trainer.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PlanoDeTreinoRequest(
    @NotNull(message = "Aluno é obrigatório")
    Long alunoId,

    @NotBlank(message = "Nome do plano é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String nome,

    @NotNull(message = "Duração é obrigatória")
    @Min(value = 1, message = "Duração mínima é 1 semana")
    @Max(value = 52, message = "Duração máxima é 52 semanas")
    Integer duracaoSemanas,

    @NotNull(message = "Data de início é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataInicio
) {}