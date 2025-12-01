package br.edu.ufape.personal_trainer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.personal_trainer.model.Fatura;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long>{
	
	List<Fatura> findByAluno_UsuarioId(Long alunoId);
	
	List<Fatura> findByStatus(String status);
}
