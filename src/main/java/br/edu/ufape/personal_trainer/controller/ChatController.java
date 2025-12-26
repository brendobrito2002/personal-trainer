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

    @Autowired private ChatService chatService;

    @GetMapping
    public List<ChatResponse> listarTodos() {
        return chatService.listarTodos().stream()
                .map(ChatResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> buscarId(@PathVariable Long id) {
        Chat chat = chatService.buscarId(id);
        return ResponseEntity.ok(new ChatResponse(chat));
    }

    @PostMapping
    public ResponseEntity<ChatResponse> criar(@Valid @RequestBody ChatRequest request) {
        Chat chat = chatService.criar(request);
        return ResponseEntity.status(201).body(new ChatResponse(chat));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        chatService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aluno/{alunoId}")
    public List<ChatResponse> buscarPorAlunoId(@PathVariable Long alunoId) {
        return chatService.buscarPorAluno(alunoId).stream()
                .map(ChatResponse::new)
                .toList();
    }

    @GetMapping("/personal/{personalId}")
    public List<ChatResponse> buscarPorPersonalId(@PathVariable Long personalId) {
        return chatService.buscarPorPersonal(personalId).stream()
                .map(ChatResponse::new)
                .toList();
    }

    @GetMapping("/entre/{alunoId}/{personalId}")
    public ResponseEntity<ChatResponse> buscarPorAlunoIdAndPersonalId(@PathVariable Long alunoId, @PathVariable Long personalId) {
        Chat chat = chatService.buscarPorAlunoIdAndPersonalId(alunoId, personalId);
        return ResponseEntity.ok(new ChatResponse(chat));
    }
}
