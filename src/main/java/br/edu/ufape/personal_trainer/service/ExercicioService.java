package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.dto.ExercicioRequest;
import br.edu.ufape.personal_trainer.model.Exercicio;
import br.edu.ufape.personal_trainer.model.GrupoMuscular;
import br.edu.ufape.personal_trainer.repository.ExercicioRepository;
import br.edu.ufape.personal_trainer.repository.GrupoMuscularRepository;

@Service
public class ExercicioService {

	@Autowired
	private ExercicioRepository exercicioRepository;
	
	@Autowired
	private GrupoMuscularRepository grupoMuscularRepository;
	
	// listar todos
	public List<Exercicio> listarTodos(){
		return exercicioRepository.findAll();
	}
	
	// buscar id
	public Exercicio buscarId(Long id) {
		return exercicioRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe exercicio com ID: " + id));
	}
	
	// criar dto
	public Exercicio criar(ExercicioRequest request) {
        GrupoMuscular grupoMuscular = grupoMuscularRepository.findById(request.grupoMuscularId())
                .orElseThrow(() -> new RuntimeException("Grupo muscular não encontrado"));

        Exercicio exercicio = new Exercicio();
        exercicio.setNome(request.nome());
        exercicio.setDescricao(request.descricao());
        exercicio.setGrupoMuscular(grupoMuscular);

        return exercicioRepository.save(exercicio);
    }
	
	// salvar
	public Exercicio salvar(Exercicio exercicio) {
	    if (exercicio.getNome() == null || exercicio.getNome().trim().isEmpty()) {
	        throw new IllegalArgumentException("Nome do exercício é obrigatório");
	    }
	    if (exercicio.getGrupoMuscular() == null) {
	        throw new IllegalArgumentException("Grupo muscular é obrigatório");
	    }
	    if (exercicioRepository.findByNomeContainingIgnoreCase(exercicio.getNome()).isEmpty()) {
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
	public List<Exercicio> buscarPorGrupoMuscular(Long grupoMuscularId){
		return exercicioRepository.findByGrupoMuscular_GrupoMuscularId(grupoMuscularId);
	}
	
	public List<Exercicio> buscarPorNome(String nome) {
	    return exercicioRepository.findByNomeContainingIgnoreCase(nome);
	}
}
