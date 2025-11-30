package br.edu.ufape.personal_trainer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public GrupoMuscular buscarId(@PathVariable Long id) {
		return grupoMuscularService.buscarId(id);
	}
	
	@PostMapping
	public GrupoMuscular salvar(@RequestBody GrupoMuscular grupoMuscular) {
		return grupoMuscularService.salvar(grupoMuscular);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		grupoMuscularService.deletar(id);
	}
	
	@GetMapping("/nome")
	public GrupoMuscular buscarPorNome(@RequestParam String nome){
		return grupoMuscularService.buscarPorNome(nome);
	}
}
