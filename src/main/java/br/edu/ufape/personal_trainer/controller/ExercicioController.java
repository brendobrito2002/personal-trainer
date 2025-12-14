package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.dto.ExercicioRequest;
import br.edu.ufape.personal_trainer.dto.ExercicioResponse;
import br.edu.ufape.personal_trainer.service.ExercicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    @Autowired
    private ExercicioService exercicioService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping
    public List<ExercicioResponse> listarTodos() {
        return exercicioService.listarTodos().stream()
                .map(ExercicioResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/{id}")
    public ResponseEntity<ExercicioResponse> buscarId(@PathVariable Long id) {
        return ResponseEntity.ok(new ExercicioResponse(exercicioService.buscarId(id)));
    }

    @PreAuthorize("hasRole('PERSONAL')")
    @PostMapping
    public ResponseEntity<ExercicioResponse> criar(@Valid @RequestBody ExercicioRequest request) {
        return ResponseEntity.status(201)
                .body(new ExercicioResponse(exercicioService.criar(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        exercicioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/grupoMuscular/{grupoMuscularId}")
    public List<ExercicioResponse> buscarPorGrupoMuscular(@PathVariable Long grupoMuscularId) {
        return exercicioService.buscarPorGrupoMuscular(grupoMuscularId).stream()
                .map(ExercicioResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/nome")
    public List<ExercicioResponse> buscarPorNome(@RequestParam String nome) {
        return exercicioService.buscarPorNome(nome).stream()
                .map(ExercicioResponse::new)
                .toList();
    }
}