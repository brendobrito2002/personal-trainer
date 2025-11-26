package br.edu.ufape.personal_trainer.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class GrupoMuscular {
	@Id
	@GeneratedValue
	private Long idGrupo;
	
	@OneToMany(mappedBy = "grupoMuscular")
	private List<Exercicio> exercicios = new ArrayList<>();
	
	private String nome;
	private String descricao;
}
