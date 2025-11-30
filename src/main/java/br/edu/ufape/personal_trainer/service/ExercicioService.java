package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.Exercicio;
import br.edu.ufape.personal_trainer.repository.ExercicioRepository;

@Service
public class ExercicioService {

	@Autowired
	private ExercicioRepository exercicioRepository;
	
	// listar todos
	public List<Exercicio> listarTodos(){
		return exercicioRepository.findAll();
	}
	
	// buscar id
	public Exercicio buscarId(Long id) {
		return exercicioRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe exercicio com ID: " + id));
	}
	
	// salvar
	public Exercicio salvar(Exercicio exercicio) {
	    if (exercicio.getNome() == null || exercicio.getNome().trim().isEmpty()) {
	        throw new IllegalArgumentException("Nome do exercício é obrigatório");
	    }
	    if (exercicio.getGrupoMuscular() == null) {
	        throw new IllegalArgumentException("Grupo muscular é obrigatório");
	    }
	    if (exercicioRepository.findByNome(exercicio.getNome()).isPresent()) {
	        throw new IllegalArgumentException("Já existe um exercício com o nome: " + exercicio.getNome());
	    }
	    return exercicioRepository.save(exercicio);
	}
	
	// deletar
	public void deletar(Long id) {
		if(!exercicioRepository.existsById(id)) {
			throw new RuntimeException("Não existe exercicio com ID: " + id);
		}
		exercicioRepository.deleteById(id);
	}
	
	// metodos personalizados
	public List<Exercicio> buscarPorGrupoMuscular(Long id){
		return exercicioRepository.findByGrupoMuscularId(id);
	}
	
	public Exercicio buscarNome(String nome) {
		return exercicioRepository.findByNome(nome).orElseThrow(() -> new RuntimeException("Não existe exercicio com nome: " + nome));
	}
}
