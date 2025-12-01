package br.edu.ufape.personal_trainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.personal_trainer.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{
	
	List<Chat> findByAluno_IdUsuario(Long alunoId);
	
    List<Chat> findByPersonal_IdUsuario(Long personalId);

    Optional<Chat> findByAluno_IdUsuarioAndPersonal_IdUsuario(Long alunoId, Long personalId);

}