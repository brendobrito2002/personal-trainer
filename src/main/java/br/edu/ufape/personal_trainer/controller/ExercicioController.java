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

import br.edu.ufape.personal_trainer.model.Exercicio;
import br.edu.ufape.personal_trainer.service.ExercicioService;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

	@Autowired
	private ExercicioService exercicioService;
	
	@GetMapping
	public List<Exercicio> listarTodos(){
		return exercicioService.listarTodos();
	}
	
	@GetMapping("/{id}")
	public Exercicio buscarId(@PathVariable Long id) {
		return exercicioService.buscarId(id);
	}
	
	@PostMapping
	public Exercicio salvar(@RequestBody Exercicio exercicio) {
		return exercicioService.salvar(exercicio);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		exercicioService.deletar(id);
	}
	
	@GetMapping("/grupoMuscular/{grupoMuscularId}")
	public List<Exercicio> buscarPorGrupoMuscular(@PathVariable Long grupoMuscularId){
		return exercicioService.buscarPorGrupoMuscular(grupoMuscularId);
	}
	
	@GetMapping("/nome")
	public Exercicio buscarNome(@RequestParam String nome) {
		return exercicioService.buscarNome(nome);
	}
}
