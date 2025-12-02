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
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.personal_trainer.dto.ItemTreinoRequest;
import br.edu.ufape.personal_trainer.dto.ItemTreinoResponse;
import br.edu.ufape.personal_trainer.model.ItemTreino;
import br.edu.ufape.personal_trainer.service.ItemTreinoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/itens")
public class ItemTreinoController {

	@Autowired
	private ItemTreinoService itemTreinoService;
	
	@GetMapping
	public List<ItemTreinoResponse> listarTodos(){
		return itemTreinoService.listarTodos().stream()
				.map(ItemTreinoResponse::new)
				.toList();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ItemTreinoResponse> buscarId(@PathVariable Long id) {
		ItemTreino itemTreino = itemTreinoService.buscarId(id);
		return ResponseEntity.ok(new ItemTreinoResponse(itemTreino));
	}
	
	@PostMapping("/plano/{planoId}/itens")
	public ResponseEntity<ItemTreinoResponse> criar(@PathVariable Long planoId, @Valid @RequestBody ItemTreinoRequest request) {
	    ItemTreino itemTreino = itemTreinoService.criar(request, planoId);
	    return ResponseEntity.status(201).body(new ItemTreinoResponse(itemTreino));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		itemTreinoService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/plano/{planoId}")
	public List<ItemTreinoResponse> buscarPorPlanoId(@PathVariable Long planoId){
		return itemTreinoService.buscarPorPlanoId(planoId).stream()
				.map(ItemTreinoResponse::new)
				.toList();
	}
	
	@GetMapping("/exercicio/{exercicioId}")
	public List<ItemTreinoResponse> buscarPorExercicioId(@PathVariable Long exercicioId){
		return itemTreinoService.buscarPorExercicioId(exercicioId).stream()
				.map(ItemTreinoResponse::new)
				.toList();
	}
}
