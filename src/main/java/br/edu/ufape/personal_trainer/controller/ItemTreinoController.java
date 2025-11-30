package br.edu.ufape.personal_trainer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.personal_trainer.model.ItemTreino;
import br.edu.ufape.personal_trainer.service.ItemTreinoService;

@RestController
@RequestMapping("/api/itens")
public class ItemTreinoController {

	@Autowired
	private ItemTreinoService itemTreinoService;
	
	@GetMapping
	public List<ItemTreino> listarTodos(){
		return itemTreinoService.listarTodos();
	}
	
	@GetMapping("/{id}")
	public ItemTreino buscarId(@PathVariable Long id) {
		return itemTreinoService.buscarId(id);
	}
	
	@PostMapping
	public ItemTreino salvar(@RequestBody ItemTreino itemTreino) {
		return itemTreinoService.salvar(itemTreino);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		itemTreinoService.deletar(id);
	}
	
	@GetMapping("/plano/{planoId}")
	public List<ItemTreino> buscarPorPlanoId(@PathVariable Long planoId){
		return itemTreinoService.buscarPorPlanoId(planoId);
	}
	
	@GetMapping("/exercicio/{exercicioId}")
	public List<ItemTreino> buscarPorExercicioId(@PathVariable Long exercicioId){
		return itemTreinoService.buscarPorExercicioId(exercicioId);
	}
}
