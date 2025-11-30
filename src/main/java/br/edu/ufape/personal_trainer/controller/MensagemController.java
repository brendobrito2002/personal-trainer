package br.edu.ufape.personal_trainer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.personal_trainer.model.Mensagem;
import br.edu.ufape.personal_trainer.service.MensagemService;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

	@Autowired
	private MensagemService mensagemService;
	
	@GetMapping
	public List<Mensagem> listarTodos(){
		return mensagemService.listarTodos();
	}
	
	@GetMapping("/{id}")
	public Mensagem buscarId(@PathVariable Long id) {
		return mensagemService.buscarId(id);
	}
	
	@PostMapping
	public Mensagem salvar(@RequestBody Mensagem mensagem) {
		return mensagemService.salvar(mensagem);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		mensagemService.deletar(id);
	}
	
	@GetMapping("/chat/{chatId}")
	public List<Mensagem> buscarPorChatId(@PathVariable Long chatId){
		return mensagemService.buscarPorChatId(chatId);
	}
	
	@GetMapping("/enviadoAluno/{chatId}")
	public List<Mensagem> buscarEnviadasPeloAluno(@PathVariable Long chatId){
		return mensagemService.buscarEnviadasPeloAluno(chatId);
	}
	
	@GetMapping("/enviadoPersonal/{chatId}")
	public List<Mensagem> buscarEnviadasPeloPersonal(@PathVariable Long chatId){
		return mensagemService.buscarEnviadasPeloPersonal(chatId);
	}
}
