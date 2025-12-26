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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.personal_trainer.model.GrupoMuscular;
import br.edu.ufape.personal_trainer.service.GrupoMuscularService;

@RestController
@RequestMapping("/api/grupos")
public class GrupoMuscularController {

	@Autowired
	private GrupoMuscularService grupoMuscularService;
	
	@GetMapping
	public List<GrupoMuscular> listarTodos(){
		return grupoMuscularService.listarTodos();
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<GrupoMuscular> buscarId(@PathVariable Long id) {
        GrupoMuscular grupo = grupoMuscularService.buscarId(id);
        return ResponseEntity.ok(grupo);
    }

    @PostMapping
    public ResponseEntity<GrupoMuscular> salvar(@RequestBody GrupoMuscular grupoMuscular) {
        GrupoMuscular salvo = grupoMuscularService.salvar(grupoMuscular);
        return ResponseEntity.status(201).body(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        grupoMuscularService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    public ResponseEntity<GrupoMuscular> buscarPorNome(@RequestParam String nome) {
        GrupoMuscular grupo = grupoMuscularService.buscarPorNome(nome);
        return ResponseEntity.ok(grupo);
    }
}
