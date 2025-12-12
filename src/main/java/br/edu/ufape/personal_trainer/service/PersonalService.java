package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.dto.PersonalRequest;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;

@Service
public class PersonalService {

	@Autowired
	private PersonalRepository personalRepository;
	
	// listar todos
	public List<Personal> listarTodos(){
		return personalRepository.findAll();
	}
	
	// buscar id
	public Personal buscarId(Long id) {
		return personalRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe personal com ID: " + id));
	}
	
	//criar dto
	public Personal criar(PersonalRequest request) {
        if (personalRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (personalRepository.findByCref(request.cref()).isPresent()) {
            throw new IllegalArgumentException("CREF já cadastrado");
        }

        Personal personal = new Personal();
        personal.setNome(request.nome());
        personal.setEmail(request.email());
        personal.setSenha(request.senha());
        personal.setCref(request.cref());

        return personalRepository.save(personal);
    }
	
	// salvar
	public Personal salvar(Personal personal) {
	    if (personal.getCref() == null || personal.getCref().trim().isEmpty()) {
	        throw new IllegalArgumentException("CREF é obrigatório");
	    }
	    if (personal.getEmail() == null || personal.getEmail().trim().isEmpty()) {
	        throw new IllegalArgumentException("Email é obrigatório");
	    }
	    if (personalRepository.findByCref(personal.getCref()).isPresent()) {
	        throw new IllegalArgumentException("CREF já cadastrado");
	    }
	    if (personalRepository.findByEmail(personal.getEmail()).isPresent()) {
	        throw new IllegalArgumentException("Email já cadastrado");
	    }
	    return personalRepository.save(personal);
	}
	
	// deletar
	public void deletar(Long id) {
		Personal personal = buscarId(id);
		
		if(!personalRepository.existsById(id)) {
			throw new RuntimeException("Não existe personal com ID: " + id);
		}
		if (!personal.getAlunos().isEmpty()) {
	        throw new IllegalStateException("Personal possui alunos vinculados — não pode ser deletado");
	    }
		personalRepository.deleteById(id);
	}
	
	// metodos personalizados
	public Personal buscarPorCref(String cref) {
		return personalRepository.findByCref(cref).orElseThrow(() -> new RuntimeException("Não existe personal com CREF: " + cref));
	}
	
	public Personal buscarPorEmail(String email) {
		return personalRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Não existe personal com EMAIL: " + email));
	}
}
