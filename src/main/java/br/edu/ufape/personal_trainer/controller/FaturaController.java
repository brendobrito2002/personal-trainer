package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.dto.FaturaRequest;
import br.edu.ufape.personal_trainer.dto.FaturaResponse;
import br.edu.ufape.personal_trainer.model.Fatura;
import br.edu.ufape.personal_trainer.service.FaturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/faturas")
public class FaturaController {

    @Autowired
    private FaturaService faturaService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping
    public List<FaturaResponse> listarTodos() {
        return faturaService.listarTodos().stream()
                .map(FaturaResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/{id}")
    public ResponseEntity<FaturaResponse> buscarId(@PathVariable Long id) {
        Fatura fatura = faturaService.buscarId(id);
        return ResponseEntity.ok(new FaturaResponse(fatura));
    }

    @PreAuthorize("hasRole('PERSONAL')")
    @PostMapping
    public ResponseEntity<FaturaResponse> criar(@Valid @RequestBody FaturaRequest request) {
        Fatura fatura = faturaService.criar(request);
        return ResponseEntity.status(201).body(new FaturaResponse(fatura));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        faturaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL', 'ALUNO')")
    @GetMapping("/aluno/{alunoId}")
    public List<FaturaResponse> buscarPorAlunoId(@PathVariable Long alunoId) {
        return faturaService.buscarPorAlunoId(alunoId).stream()
                .map(FaturaResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping("/status")
    public List<FaturaResponse> buscarPorStatus(@RequestParam String status) {
        return faturaService.buscarPorStatus(status).stream()
                .map(FaturaResponse::new)
                .toList();
    }
}