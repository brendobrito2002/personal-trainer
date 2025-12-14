package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.PlanoDeTreinoRequest;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.PlanoDeTreino;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
import br.edu.ufape.personal_trainer.repository.PlanoDeTreinoRepository;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class PlanoDeTreinoService {

    @Autowired
    private PlanoDeTreinoRepository planoDeTreinoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<PlanoDeTreino> listarTodos() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return planoDeTreinoRepository.findAll();
        }
        if (logado.getRole() == Role.PERSONAL) {
            Personal personal = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            List<Aluno> alunosDoPersonal = alunoRepository.findByPersonal(personal);
            return planoDeTreinoRepository.findByAlunoIn(alunosDoPersonal);
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public PlanoDeTreino buscarId(Long id) {
        PlanoDeTreino plano = planoDeTreinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano de treino não encontrado com ID: " + id));

        Usuario logado = getUsuarioLogado();
        Aluno aluno = plano.getAluno();

        if (logado.getRole() == Role.ALUNO && !aluno.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar seu próprio plano de treino");
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar planos de seus próprios alunos");
            }
        }

        return plano;
    }

    @Transactional
    public PlanoDeTreino criar(PlanoDeTreinoRequest request) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.PERSONAL && logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas personal ou admin pode criar planos de treino");
        }

        Aluno aluno = alunoRepository.findById(request.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode criar planos para seus próprios alunos");
            }
        }

        if (aluno.getPersonal() == null) {
            throw new IllegalArgumentException("Aluno precisa estar vinculado a um personal");
        }

        if (planoDeTreinoRepository.findByAluno_UsuarioIdAndAtivoTrue(aluno.getUsuarioId()).isPresent()) {
            throw new IllegalStateException("Aluno já possui um plano ativo");
        }

        PlanoDeTreino plano = new PlanoDeTreino();
        plano.setAluno(aluno);
        plano.setNome(request.nome());
        plano.setDuracaoSemanas(request.duracaoSemanas());
        plano.setDataInicio(request.dataInicio());
        plano.setDataFim(request.dataInicio().plusWeeks(request.duracaoSemanas()));
        plano.setAtivo(true);

        return planoDeTreinoRepository.save(plano);
    }

    @Transactional
    public PlanoDeTreino salvar(PlanoDeTreino plano) {
        if (plano.getAluno() == null) {
            throw new IllegalArgumentException("Plano deve ter um aluno");
        }
        if (plano.getNome() == null || plano.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano é obrigatório");
        }
        return planoDeTreinoRepository.save(plano);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas admin pode deletar planos de treino");
        }

        if (!planoDeTreinoRepository.existsById(id)) {
            throw new RuntimeException("Plano de treino não existe com ID: " + id);
        }
        planoDeTreinoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PlanoDeTreino> buscarPorAlunoId(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !aluno.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar seus próprios planos de treino");
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar planos de seus próprios alunos");
            }
        }

        return planoDeTreinoRepository.findByAluno_UsuarioId(alunoId);
    }
}