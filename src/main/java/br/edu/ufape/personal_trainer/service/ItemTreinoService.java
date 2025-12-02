package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.dto.ItemTreinoRequest;
import br.edu.ufape.personal_trainer.model.Exercicio;
import br.edu.ufape.personal_trainer.model.ItemTreino;
import br.edu.ufape.personal_trainer.model.PlanoDeTreino;
import br.edu.ufape.personal_trainer.repository.ExercicioRepository;
import br.edu.ufape.personal_trainer.repository.ItemTreinoRepository;
import br.edu.ufape.personal_trainer.repository.PlanoDeTreinoRepository;

@Service
public class ItemTreinoService {

	@Autowired
	private ItemTreinoRepository itemTreinoRepository;
	
	@Autowired
	private ExercicioRepository exercicioRepository;
	
	@Autowired
	private PlanoDeTreinoRepository planoDeTreinoRepository;
	
	// listar todos
	public List<ItemTreino> listarTodos(){
		return itemTreinoRepository.findAll();
	}
	
	// buscar id
	public ItemTreino buscarId(Long id) {
		return itemTreinoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe item treino com ID: " + id));
	}
	
	// criar dto
	public ItemTreino criar(ItemTreinoRequest request, Long planoId) {  // ← FALTOU planoId
	    Exercicio exercicio = exercicioRepository.findById(request.exercicioId())
	        .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

	    PlanoDeTreino plano = planoDeTreinoRepository.findById(planoId)
	        .orElseThrow(() -> new RuntimeException("Plano não encontrado"));

	    ItemTreino itemTreino = new ItemTreino();
	    itemTreino.setExercicio(exercicio);
	    itemTreino.setPlano(plano);  // ← FALTOU ISSO
	    itemTreino.setSeries(request.series());
	    itemTreino.setRepeticoes(request.repeticoes());
	    itemTreino.setCargaKg(request.cargaKg());
	    itemTreino.setDescansoSegundos(request.descansoSegundos());

	    return itemTreinoRepository.save(itemTreino);
	}
	
	// salvar
	public ItemTreino salvar(ItemTreino itemTreino) {
		if (itemTreino.getPlano() == null) {
		    throw new IllegalArgumentException("ItemTreino deve ter um plano");
		}
		if (itemTreino.getExercicio() == null) {
		    throw new IllegalArgumentException("ItemTreino deve ter um exercício");
		}
		return itemTreinoRepository.save(itemTreino);
	}
	
	// deletar
	public void deletar(Long id) {
		if(!itemTreinoRepository.existsById(id)) {
			throw new RuntimeException("Não existe item treino com ID: " + id);
		}
		itemTreinoRepository.deleteById(id);
	}
	
	// metodos personalizados
	public List<ItemTreino> buscarPorPlanoId(Long id){
		return itemTreinoRepository.findByPlano_PlanoId(id);
	}
	
	public List<ItemTreino> buscarPorExercicioId(Long id){
		return itemTreinoRepository.findByExercicio_ExercicioId(id);
	}
}
