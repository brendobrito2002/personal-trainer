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

import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.service.AlunoService;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

	@Autowired
	private AlunoService alunoService;
	
	@GetMapping
	public List<Aluno> listarTodos(){
		return alunoService.listarTodos();
	}
	
	@GetMapping("/{id}")
	public Aluno buscarId(@PathVariable Long id) {
		return alunoService.buscarId(id);
	}
	
	@PostMapping
	public Aluno salvar(@RequestBody Aluno aluno) {
		return alunoService.salvar(aluno);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		alunoService.deletar(id);
	}
	
	@GetMapping("/modalidades/{modalidade}")
	public List<Aluno> listarPorModalidade(@PathVariable String modalidade){
		return alunoService.listarPorModalidade(modalidade);
	}
	
	@GetMapping("/ativos")
	public List<Aluno> listarPorAtivo(){
		return alunoService.listarPorAtivo();
	}
	
	@GetMapping("/email/{email}")
	public Aluno buscarEmail(@PathVariable String email) {
		return alunoService.buscarEmail(email);
	}
	
	@GetMapping("/nome")
	public List<Aluno> buscarNome(@RequestParam String nome){
		return alunoService.buscarNome(nome);
	}
}
