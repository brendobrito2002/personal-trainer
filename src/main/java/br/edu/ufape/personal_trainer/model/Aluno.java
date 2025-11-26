package br.edu.ufape.personal_trainer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Aluno extends Usuario{
	@ManyToOne
	@JoinColumn(name = "idPersonal")
	private Personal personal;
	
	@OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AvaliacaoFisica> avaliacoes;
	
	@OneToOne(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
	private Chat chat;
	
	@OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Fatura> faturas = new ArrayList<>();
	
	@OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PlanoDeTreino> planos = new ArrayList<>();
	
	private String modalidade;
	private Date dataNascimento;
	private Boolean ativo;
	private String objetivo;
}
