package br.edu.ufape.personal_trainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Fatura;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    
    List<Fatura> findByAluno_UsuarioId(Long alunoId);
    
    List<Fatura> findByStatus(String status);
    
    Optional<Fatura> findByAluno_UsuarioIdAndStatus(Long alunoId, String status);
    
    List<Fatura> findByAlunoIn(List<Aluno> alunos);
    
    List<Fatura> findByAlunoInAndStatus(List<Aluno> alunos, String status);
}
