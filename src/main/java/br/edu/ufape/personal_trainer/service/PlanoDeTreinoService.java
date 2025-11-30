package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.PlanoDeTreino;
import br.edu.ufape.personal_trainer.repository.PlanoDeTreinoRepository;

@Service
public class PlanoDeTreinoService {

    @Autowired
    private PlanoDeTreinoRepository planoDeTreinoRepository;

    // listar todos
    public List<PlanoDeTreino> listarTodos() {
        return planoDeTreinoRepository.findAll();
    }

    // buscar id
    public PlanoDeTreino buscarId(Long id) {
        return planoDeTreinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano de treino não encontrado com ID: " + id));
    }

    // salvar (TALVEZ ADICIONAR MAIS)
    public PlanoDeTreino salvar(PlanoDeTreino plano) {
        if (plano.getAluno() == null) {
            throw new IllegalArgumentException("Plano deve ter um aluno");
        }
        if (plano.getNome() == null || plano.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano é obrigatório");
        }
        return planoDeTreinoRepository.save(plano);
    }

    // deletar
    public void deletar(Long id) {
        if (!planoDeTreinoRepository.existsById(id)) {
            throw new RuntimeException("Plano de treino não existe com ID: " + id);
        }
        planoDeTreinoRepository.deleteById(id);
    }

    // metodos personalizados
    public List<PlanoDeTreino> buscarPorAlunoId(Long alunoId) {
        return planoDeTreinoRepository.findByAlunoId(alunoId);
    }

    public List<PlanoDeTreino> buscarPorNome(String nome) {
        return planoDeTreinoRepository.findByNome(nome);
    }
}