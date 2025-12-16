package br.edu.ufape.personal_trainer.dto;

import br.edu.ufape.personal_trainer.model.ItemTreino;

public record ItemTreinoResponse(
    Long id,
    Long planoDeTreinoId,
    String nomePlanoDeTreino,
    Long exercicioId,
    String nomeExercicio,
    String nomeGrupoMuscular,
    Integer series,
    String repeticoes,
    Double cargaKg,
    Integer descansoSegundos
) {
    public ItemTreinoResponse(ItemTreino i) {
        this(
            i.getItemTreinoId(),
            i.getPlano().getPlanoId(),
            i.getPlano().getNome(),
            i.getExercicio().getExercicioId(),
            i.getExercicio().getNome(),
            i.getExercicio().getGrupoMuscular().getNome(),
            i.getSeries(),
            i.getRepeticoes(),
            i.getCargaKg(),
            i.getDescansoSegundos()
        );
    }
}