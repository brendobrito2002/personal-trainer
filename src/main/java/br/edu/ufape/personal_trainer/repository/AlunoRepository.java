package br.edu.ufape.personal_trainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Personal;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long>{
	
	List<Aluno> findByModalidade(String modalidade);
	
	List<Aluno> findByAtivoTrue();
	
	Optional<Aluno> findByEmail(String email);
	
	List<Aluno> findByNome(String nome);

	@Query("SELECT a FROM Aluno a WHERE a.personal = :personal AND a.modalidade = :modalidade")
	List<Aluno> findByPersonalAndModalidade(@Param("personal") Personal personal, @Param("modalidade") String modalidade);
	
	List<Aluno> findByPersonal(Personal personal);
}