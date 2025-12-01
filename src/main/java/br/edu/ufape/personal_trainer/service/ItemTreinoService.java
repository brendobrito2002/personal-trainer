package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.ItemTreino;
import br.edu.ufape.personal_trainer.repository.ItemTreinoRepository;

@Service
public class ItemTreinoService {

	@Autowired
	private ItemTreinoRepository itemTreinoRepository;
	
	// listar todos
	public List<ItemTreino> listarTodos(){
		return itemTreinoRepository.findAll();
	}
	
	// buscar id
	public ItemTreino buscarId(Long id) {
		return itemTreinoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe item treino com ID: " + id));
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
		return itemTreinoRepository.findByPlano_IdPlano(id);
	}
	
	public List<ItemTreino> buscarPorExercicioId(Long id){
		return itemTreinoRepository.findByExercicio_IdExercicio(id);
	}
}
