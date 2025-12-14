package br.edu.ufape.personal_trainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.PlanoDeTreino;

@Repository
public interface PlanoDeTreinoRepository extends JpaRepository<PlanoDeTreino, Long>{
	
	List<PlanoDeTreino> findByAluno_UsuarioId(Long alunoId);
	
	List<PlanoDeTreino> findByNome(String nome);
	
	Optional<PlanoDeTreino> findByAluno_UsuarioIdAndAtivoTrue(Long alunoId);
	
	List<PlanoDeTreino> findByAlunoIn(List<Aluno> alunos);
}