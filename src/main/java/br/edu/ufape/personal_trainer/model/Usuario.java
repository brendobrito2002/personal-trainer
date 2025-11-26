package br.edu.ufape.personal_trainer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public abstract class Usuario {
	@Id
	@GeneratedValue
	private Long idUsuario;
	
	private String nome;
	private String email;
	private String senha;
}