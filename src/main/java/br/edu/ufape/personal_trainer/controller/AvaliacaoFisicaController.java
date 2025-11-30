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

import br.edu.ufape.personal_trainer.model.AvaliacaoFisica;
import br.edu.ufape.personal_trainer.service.AvaliacaoFisicaService;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoFisicaController {

	@Autowired
	private AvaliacaoFisicaService avaliacaoFisicaService;
	
	@GetMapping
	public List<AvaliacaoFisica> listarTodos(){
		return avaliacaoFisicaService.listarTodos();
	}
	
	@GetMapping("/{id}")
	public AvaliacaoFisica buscarId(@PathVariable Long id) {
		return avaliacaoFisicaService.buscarId(id);
	}
	
	@PostMapping
	public AvaliacaoFisica salvar(@RequestBody AvaliacaoFisica avaliacaoFisica) {
		return avaliacaoFisicaService.salvar(avaliacaoFisica);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		avaliacaoFisicaService.deletar(id);
	}
	
	@GetMapping("/aluno/{alunoId}")
	public List<AvaliacaoFisica> encontrarPorIdAluno(@PathVariable Long alunoId){
		return avaliacaoFisicaService.encontrarPorIdAluno(alunoId);
	}
}
