package br.edu.ufape.personal_trainer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.edu.ufape.personal_trainer.model.PlanoDeTreino;

import java.time.LocalDate;

public record PlanoDeTreinoResponse(
    Long id,
    Long alunoId,
    String nome,
    Integer duracaoSemanas,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataInicio,

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataFim,

    Boolean ativo
) {
    public PlanoDeTreinoResponse(PlanoDeTreino p) {
        this(
            p.getPlanoId(),
            p.getAluno().getUsuarioId(),
            p.getNome(),
            p.getDuracaoSemanas(),
            p.getDataInicio(),
            p.getDataFim(),
            p.getAtivo()
        );
    }
}