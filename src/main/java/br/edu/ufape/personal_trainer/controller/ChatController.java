package br.edu.ufape.personal_trainer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.personal_trainer.dto.ChatRequest;
import br.edu.ufape.personal_trainer.dto.ChatResponse;
import br.edu.ufape.personal_trainer.model.Chat;
import br.edu.ufape.personal_trainer.service.ChatService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

	@Autowired
	private ChatService chatService;
	
	@GetMapping
	public List<Chat> listarTodos(){
		return chatService.listarTodos();
	}
	
	@GetMapping("/{id}")
	public Chat buscarId(@PathVariable Long id) {
		return chatService.buscarId(id);
	}
	
	@PostMapping
	public ResponseEntity<ChatResponse> criar(@Valid @RequestBody ChatRequest request) {
	    Chat chat = chatService.criar(request);
	    return ResponseEntity.status(201).body(new ChatResponse(chat));
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		chatService.deletar(id);
	}
	
	@GetMapping("/aluno/{alunoId}")
	public List<Chat> buscarPorAlunoId(@PathVariable Long alunoId){
		return chatService.buscarPorAluno(alunoId);
	}
	
	@GetMapping("/personal/{personalId}")
	public List<Chat> buscarPorPersonalId(@PathVariable Long personalId){
		return chatService.buscarPorPersonal(personalId);
	}
	
	@GetMapping("/entre/{alunoId}/{personalId}")
	public Chat buscarPorAlunoIdAndPersonalId(@PathVariable Long alunoId, @PathVariable Long personalId) {
		return chatService.buscarPorAlunoIdAndPersonalId(alunoId, personalId);
	}
}
