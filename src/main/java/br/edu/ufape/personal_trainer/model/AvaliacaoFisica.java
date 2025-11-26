package br.edu.ufape.personal_trainer.model;

import java.util.Date;
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
public class AvaliacaoFisica {
	@Id
	@GeneratedValue
	private Long idAvaliacao;
	
	@ManyToOne
	@JoinColumn(name = "idAluno")
	private Aluno aluno;
	
	private Date dataAvaliacao;
	private Double pesoKg;
	private Double alturaCm;
	private Double percentualGordura;
	private String observacoes;
	private String foto;
	private Boolean feitoPeloPersonal;
}
