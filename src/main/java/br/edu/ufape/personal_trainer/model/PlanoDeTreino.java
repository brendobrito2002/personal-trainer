package br.edu.ufape.personal_trainer.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class PlanoDeTreino {
	@Id
	@GeneratedValue
	private Long idPlanoDeTreino;
	
	@ManyToOne
	@JoinColumn(name = "idAluno")
	private Aluno aluno;
	
	@OneToMany(mappedBy = "idPlanoDeTreino", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemTreino> itens;
	
	private String nome;
	private Date dataCriacao;
	private int duracaoSemanas;
	private Date dataInicio;
	private Date dataFim;
	private Boolean ativo;
}
