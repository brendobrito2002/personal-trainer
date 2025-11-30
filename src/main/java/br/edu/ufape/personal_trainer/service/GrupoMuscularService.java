package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.GrupoMuscular;
import br.edu.ufape.personal_trainer.repository.GrupoMuscularRepository;

@Service
public class GrupoMuscularService {

	@Autowired
	private GrupoMuscularRepository grupoMuscularRepository;
	
	// listar todos
	public List<GrupoMuscular> listarTodos(){
		return grupoMuscularRepository.findAll();
	}
	
	// buscar id
	public GrupoMuscular buscarId(Long id) {
		return grupoMuscularRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe grupo muscular com ID: " + id));
	}
	
	// salvar
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
	public void deletar(Long id) {
		if(!grupoMuscularRepository.existsById(id)) {
			throw new RuntimeException("Não existe grupo muscular com ID: " + id);
		}
		grupoMuscularRepository.deleteById(id);
	}
	
	// metodos personalizados
	public GrupoMuscular buscarPorNome(String nome) {
		return grupoMuscularRepository.findByNome(nome).orElseThrow(() -> new RuntimeException("Não existe grupo muscular com NOME: " + nome));
	}
}
