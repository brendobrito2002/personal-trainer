package br.edu.ufape.personal_trainer.model;

import jakarta.persistence.Column;
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
	private Long itemTreinoId;
	
	@ManyToOne
	@JoinColumn(name = "planoId", nullable = false)
	private PlanoDeTreino plano;
	
	@ManyToOne
	@JoinColumn(name = "exercicioId", nullable = false)
	private Exercicio exercicio;
	
	@Column(nullable = false)
	private int series;
	
	@Column(nullable = false)
	private String repeticoes;
	
	@Column(nullable = true)
	private Double cargaKg;
	
	@Column(nullable = false)
	private int descansoSegundos;
}
