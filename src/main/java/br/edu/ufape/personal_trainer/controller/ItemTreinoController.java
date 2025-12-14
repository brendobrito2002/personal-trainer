package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.dto.ItemTreinoRequest;
import br.edu.ufape.personal_trainer.dto.ItemTreinoResponse;
import br.edu.ufape.personal_trainer.model.ItemTreino;
import br.edu.ufape.personal_trainer.service.ItemTreinoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/itens")
public class ItemTreinoController {

    @Autowired
    private ItemTreinoService itemTreinoService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping
    public List<ItemTreinoResponse> listarTodos() {
        return itemTreinoService.listarTodos().stream()
                .map(ItemTreinoResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/{id}")
    public ResponseEntity<ItemTreinoResponse> buscarId(@PathVariable Long id) {
        ItemTreino itemTreino = itemTreinoService.buscarId(id);
        return ResponseEntity.ok(new ItemTreinoResponse(itemTreino));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @PostMapping("/plano/{planoId}/itens")
    public ResponseEntity<ItemTreinoResponse> criar(@PathVariable Long planoId, @Valid @RequestBody ItemTreinoRequest request) {
        ItemTreino itemTreino = itemTreinoService.criar(request, planoId);
        return ResponseEntity.status(201).body(new ItemTreinoResponse(itemTreino));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemTreinoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/plano/{planoId}")
    public List<ItemTreinoResponse> buscarPorPlanoId(@PathVariable Long planoId) {
        return itemTreinoService.buscarPorPlanoId(planoId).stream()
                .map(ItemTreinoResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/exercicio/{exercicioId}")
    public List<ItemTreinoResponse> buscarPorExercicioId(@PathVariable Long exercicioId) {
        return itemTreinoService.buscarPorExercicioId(exercicioId).stream()
                .map(ItemTreinoResponse::new)
                .toList();
    }
}