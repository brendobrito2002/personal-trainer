package br.edu.ufape.personal_trainer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class ItemTreino {
	@Id
	@GeneratedValue
	private Long idItemTreino;
	
	@ManyToOne
	@JoinColumn(name = "idPlanoDeTreino", nullable = false)
	private PlanoDeTreino plano;
	
	@ManyToOne
	@JoinColumn(name = "idExercicio", nullable = false)
	private Exercicio exercicio;
	
	private int series;
	private String repeticoes;
	private Double cargaKg;
	private int descansoSegundos;
}
