package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.dto.AvaliacaoFisicaRequest;
import br.edu.ufape.personal_trainer.dto.AvaliacaoFisicaResponse;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.AvaliacaoFisica;
import br.edu.ufape.personal_trainer.service.AlunoService;
import br.edu.ufape.personal_trainer.service.AvaliacaoFisicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoFisicaController {

    @Autowired
    private AvaliacaoFisicaService avaliacaoFisicaService;

    @Autowired
    private AlunoService alunoService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping
    public List<AvaliacaoFisicaResponse> listarTodos() {
        return avaliacaoFisicaService.listarTodos().stream()
                .map(AvaliacaoFisicaResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoFisicaResponse> buscarId(@PathVariable Long id) {
        AvaliacaoFisica av = avaliacaoFisicaService.buscarId(id);
        return ResponseEntity.ok(new AvaliacaoFisicaResponse(av));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @PostMapping
    public ResponseEntity<AvaliacaoFisicaResponse> salvar(@Valid @RequestBody AvaliacaoFisicaRequest request) {
        Aluno aluno = alunoService.buscarId(request.alunoId());
        AvaliacaoFisica av = avaliacaoFisicaService.criar(request, aluno);
        return ResponseEntity.status(201).body(new AvaliacaoFisicaResponse(av));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoFisicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/aluno/{alunoId}")
    public List<AvaliacaoFisicaResponse> encontrarPorIdAluno(@PathVariable Long alunoId) {
        return avaliacaoFisicaService.encontrarPorIdAluno(alunoId).stream()
                .map(AvaliacaoFisicaResponse::new)
                .toList();
    }
}