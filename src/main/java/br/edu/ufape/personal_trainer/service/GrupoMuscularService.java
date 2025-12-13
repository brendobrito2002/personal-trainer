package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufape.personal_trainer.model.GrupoMuscular;
import br.edu.ufape.personal_trainer.repository.GrupoMuscularRepository;

@Service
public class GrupoMuscularService {

	@Autowired
	private GrupoMuscularRepository grupoMuscularRepository;
	
	// listar todos
	@Transactional(readOnly = true)
	public List<GrupoMuscular> listarTodos(){
		return grupoMuscularRepository.findAll();
	}
	
	// buscar id
	@Transactional(readOnly = true)
	public GrupoMuscular buscarId(Long id) {
		return grupoMuscularRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe grupo muscular com ID: " + id));
	}
	
	// salvar
	@Transactional
	public GrupoMuscular salvar(GrupoMuscular grupoMuscular) {
		if(grupoMuscular.getNome() == null || grupoMuscular.getNome().trim().isEmpty()) {
			throw new IllegalArgumentException("Um grupo muscular deve ter um nome");
		}
		
		if(grupoMuscularRepository.findByNome(grupoMuscular.getNome()).isPresent()) {
			throw new IllegalArgumentException("Já existe grupo muscular com nome: " + grupoMuscular.getNome());
		}
		return grupoMuscularRepository.save(grupoMuscular);
	}
	
	// deletar
	@Transactional
	public void deletar(Long id) {
		if(!grupoMuscularRepository.existsById(id)) {
			throw new RuntimeException("Não existe grupo muscular com ID: " + id);
		}
		grupoMuscularRepository.deleteById(id);
	}
	
	// metodos personalizados
	@Transactional(readOnly = true)
	public GrupoMuscular buscarPorNome(String nome) {
		return grupoMuscularRepository.findByNome(nome).orElseThrow(() -> new RuntimeException("Não existe grupo muscular com NOME: " + nome));
	}
}
