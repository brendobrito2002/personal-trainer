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

import br.edu.ufape.personal_trainer.model.Fatura;
import br.edu.ufape.personal_trainer.service.FaturaService;

@RestController
@RequestMapping("/api/faturas")
public class FaturaController {
	
	@Autowired
	private FaturaService faturaService;
	
	@GetMapping
	public List<Fatura> listarTodos(){
		return faturaService.listarTodos();
	}
	
	@GetMapping("/{id}")
	public Fatura buscarId(@PathVariable Long id) {
		return faturaService.buscarId(id);
	}
	
	@PostMapping
	public Fatura salvar(@RequestBody Fatura fatura) {
		return faturaService.salvar(fatura);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		faturaService.deletar(id);
	}
	
	@GetMapping("/aluno/{alunoId}")
	public List<Fatura> buscarPorAlunoId(@PathVariable Long alunoId){
		return faturaService.buscarPorAlunoId(alunoId);
	}
	
	@GetMapping("/status")
	public List<Fatura> buscarPorStatus(@RequestParam String status){
		return faturaService.buscarPorStatus(status);
	}
}
