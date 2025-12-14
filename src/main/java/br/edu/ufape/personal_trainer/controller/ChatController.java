package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.dto.ChatRequest;
import br.edu.ufape.personal_trainer.dto.ChatResponse;
import br.edu.ufape.personal_trainer.model.Chat;
import br.edu.ufape.personal_trainer.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping
    public List<ChatResponse> listarTodos() {
        return chatService.listarTodos().stream()
                .map(ChatResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> buscarId(@PathVariable Long id) {
        Chat chat = chatService.buscarId(id);
        return ResponseEntity.ok(new ChatResponse(chat));
    }

    @PreAuthorize("hasRole('PERSONAL')")
    @PostMapping
    public ResponseEntity<ChatResponse> criar(@Valid @RequestBody ChatRequest request) {
        Chat chat = chatService.criar(request);
        return ResponseEntity.status(201).body(new ChatResponse(chat));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        chatService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/aluno/{alunoId}")
    public List<ChatResponse> buscarPorAlunoId(@PathVariable Long alunoId) {
        return chatService.buscarPorAluno(alunoId).stream()
                .map(ChatResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping("/personal/{personalId}")
    public List<ChatResponse> buscarPorPersonalId(@PathVariable Long personalId) {
        return chatService.buscarPorPersonal(personalId).stream()
                .map(ChatResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/entre/{alunoId}/{personalId}")
    public ResponseEntity<ChatResponse> buscarPorAlunoIdAndPersonalId(
            @PathVariable Long alunoId, @PathVariable Long personalId) {
        Chat chat = chatService.buscarPorAlunoIdAndPersonalId(alunoId, personalId);
        return ResponseEntity.ok(new ChatResponse(chat));
    }
}