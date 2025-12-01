package br.edu.ufape.personal_trainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.personal_trainer.model.Exercicio;

@Repository
public interface ExercicioRepository extends JpaRepository<Exercicio, Long>{
	
	List<Exercicio> findByGrupoMuscular_GrupoMuscularId(Long grupoMuscularId);
	
	Optional<Exercicio> findByNome(String nome);
}
