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
public class Fatura {
	@Id
	@GeneratedValue
	private Long idFatura;
	
	@ManyToOne
	@JoinColumn(name = "idAluno")
	private Aluno aluno;
	
	private Date dataVencimento;
	private Date dataPagamento;
	private Double valor;
	private String status;
}
