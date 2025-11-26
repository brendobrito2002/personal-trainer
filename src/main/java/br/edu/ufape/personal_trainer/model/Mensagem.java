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
public class Mensagem{
	@Id
	@GeneratedValue
	private Long idMensagem;
	
	@ManyToOne
	@JoinColumn(name = "chat")
	private Chat chat;
	
	private String conteudo;
	private String caminhoImagem;
	private String caminhoVideo;
	private Date timeStamp;
	private Boolean enviadoPeloAluno;
	private Boolean lida;
}
