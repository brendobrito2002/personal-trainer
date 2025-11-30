package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository alunoRepository;
	
	// listar todos
	public List<Aluno> listarTodos(){
		return alunoRepository.findAll();
	}
	
	// buscar id
	public Aluno buscarId(Long id) {
		return alunoRepository.findById(id).orElseThrow(() -> new RuntimeException("Já existe um aluno com ID: " + id));
	}
	
	// salvar
	public Aluno salvar(Aluno aluno) {
		if(aluno.getEmail() == null || aluno.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email é obrigatório");
		}
		
		if(aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
			throw new IllegalArgumentException("Nome é obrigatório");
		}
		return alunoRepository.save(aluno);
	}
	
	// deletar
	public void deletar(Long id) {
		if(!alunoRepository.existsById(id)) {
			throw new RuntimeException("Não existe aluno com ID: " + id);
		}
		alunoRepository.deleteById(id);
	}
	
	// metodos personalizados
	public List<Aluno> listarPorModalidade(String modalidade){
		return alunoRepository.findByModalidade(modalidade);
	}
	
	public List<Aluno> listarPorAtivo(){
		return alunoRepository.findByAtivoTrue();
	}
	
	public Aluno buscarEmail(String email){
		return alunoRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Aluno não encontrado com email: " + email));
	}
	
	public List<Aluno> buscarNome(String nome){
		return alunoRepository.findByNome(nome);
	}
}
