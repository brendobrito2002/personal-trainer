package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.Chat;
import br.edu.ufape.personal_trainer.repository.ChatRepository;

@Service
public class ChatService {

	@Autowired
	private ChatRepository chatRepository;
	
	// listar todos
	public List<Chat> listarTodos(){
		return chatRepository.findAll();
	}
	
	// buscar id
	public Chat buscarId(Long id) {
		return chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe chat com ID: " + id));
	}
	
	// salvar
	public Chat salvar(Chat chat) {
	    if (chat.getAluno() == null || chat.getPersonal() == null) {
	        throw new IllegalArgumentException("Um chat deve ter um aluno e um personal");
	    }
	    Long alunoId = chat.getAluno().getUsuarioId();
	    Long personalId = chat.getPersonal().getUsuarioId();
	    if (chatRepository.findByAluno_UsuarioIdAndPersonal_UsuarioId(alunoId, personalId).isPresent()) {
	        throw new IllegalArgumentException("Chat já existe entre esses usuários");
	    }
	    return chatRepository.save(chat);
	}
	
	// deletar
	public void deletar(Long id) {
		if(!chatRepository.existsById(id)) {
			throw new RuntimeException("Não existe chat com o ID: "+ id);
		}
		chatRepository.deleteById(id);
	}
	
	//metodos personalizados
	public List<Chat> buscarPorAluno(Long alunoId) {
	    return chatRepository.findByAluno_UsuarioId(alunoId);
	}

	public List<Chat> buscarPorPersonal(Long personalId) {
	    return chatRepository.findByPersonal_UsuarioId(personalId);
	}
	
	public Chat buscarPorAlunoIdAndPersonalId(Long alunoId, Long personalId) {
		return chatRepository.findByAluno_UsuarioIdAndPersonal_UsuarioId(alunoId, personalId).orElseThrow(() -> new RuntimeException("Chat não encontrado entre aluno ID " + alunoId + " e personal ID " + personalId));
	}
}
