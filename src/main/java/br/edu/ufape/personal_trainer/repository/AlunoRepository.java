package br.edu.ufape.personal_trainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.personal_trainer.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long>{
	
	List<Aluno> findByModalidade(String modalidade);
	
	List<Aluno> findByAtivo();
	
	Optional<Aluno> findByEmail(String email);
	
	Optional<Aluno> findByNome(String nome);
}