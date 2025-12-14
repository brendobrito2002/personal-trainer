package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.dto.MensagemRequest;
import br.edu.ufape.personal_trainer.dto.MensagemResponse;
import br.edu.ufape.personal_trainer.model.Mensagem;
import br.edu.ufape.personal_trainer.service.MensagemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping
    public List<MensagemResponse> listarTodos() {
        return mensagemService.listarTodos().stream()
                .map(MensagemResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/{id}")
    public ResponseEntity<MensagemResponse> buscarId(@PathVariable Long id) {
        Mensagem msg = mensagemService.buscarId(id);
        return ResponseEntity.ok(new MensagemResponse(msg));
    }

    @PreAuthorize("hasAnyRole('PERSONAL', 'ALUNO')")
    @PostMapping("/chat/{chatId}")
    public ResponseEntity<MensagemResponse> enviar(
            @PathVariable Long chatId,
            @Valid @RequestBody MensagemRequest request
    ) {
        Mensagem msg = mensagemService.criar(request, chatId);
        return ResponseEntity.status(201).body(new MensagemResponse(msg));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        mensagemService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/chat/{chatId}")
    public List<MensagemResponse> buscarPorChatId(@PathVariable Long chatId) {
        return mensagemService.buscarPorChatId(chatId).stream()
                .map(MensagemResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/enviadoAluno/{chatId}")
    public List<MensagemResponse> buscarEnviadasPeloAluno(@PathVariable Long chatId) {
        return mensagemService.buscarEnviadasPeloAluno(chatId).stream()
                .map(MensagemResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/enviadoPersonal/{chatId}")
    public List<MensagemResponse> buscarEnviadasPeloPersonal(@PathVariable Long chatId) {
        return mensagemService.buscarEnviadasPeloPersonal(chatId).stream()
                .map(MensagemResponse::new)
                .toList();
    }
}