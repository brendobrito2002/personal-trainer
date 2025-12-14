package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.model.GrupoMuscular;
import br.edu.ufape.personal_trainer.service.GrupoMuscularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoMuscularController {

    @Autowired
    private GrupoMuscularService grupoMuscularService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping
    public List<GrupoMuscular> listarTodos() {
        return grupoMuscularService.listarTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping("/{id}")
    public GrupoMuscular buscarId(@PathVariable Long id) {
        return grupoMuscularService.buscarId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @PostMapping
    public GrupoMuscular salvar(@RequestBody GrupoMuscular grupoMuscular) {
        return grupoMuscularService.salvar(grupoMuscular);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        grupoMuscularService.deletar(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping("/nome")
    public GrupoMuscular buscarPorNome(@RequestParam String nome) {
        return grupoMuscularService.buscarPorNome(nome);
    }
}