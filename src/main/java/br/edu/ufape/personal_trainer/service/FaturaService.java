package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.FaturaRequest;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Fatura;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
import br.edu.ufape.personal_trainer.repository.FaturaRepository;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class FaturaService {

    @Autowired
    private FaturaRepository faturaRepository;

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
    public List<Fatura> listarTodos() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return faturaRepository.findAll();
        }
        if (logado.getRole() == Role.PERSONAL) {
            Personal personal = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            List<Aluno> alunosDoPersonal = alunoRepository.findByPersonal(personal);
            return faturaRepository.findByAlunoIn(alunosDoPersonal);
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public Fatura buscarId(Long id) {
        Fatura fatura = faturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe fatura com ID: " + id));

        Usuario logado = getUsuarioLogado();
        Aluno aluno = fatura.getAluno();

        if (logado.getRole() == Role.ALUNO && !aluno.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar suas próprias faturas");
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar faturas de seus próprios alunos");
            }
        }

        return fatura;
    }

    @Transactional
    public Fatura criar(FaturaRequest request) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.PERSONAL && logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas personal ou admin pode criar faturas");
        }

        Aluno aluno = alunoRepository.findById(request.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode criar faturas para seus próprios alunos");
            }
        }

        if (aluno.getPersonal() == null) {
            throw new IllegalArgumentException("Aluno precisa estar vinculado a um personal");
        }

        if (faturaRepository.findByAluno_UsuarioIdAndStatus(aluno.getUsuarioId(), "PENDENTE").isPresent()) {
            throw new IllegalStateException("Aluno já possui uma fatura pendente");
        }

        Fatura fatura = new Fatura();
        fatura.setAluno(aluno);
        fatura.setValor(request.valor());
        fatura.setDataVencimento(request.dataVencimento());
        fatura.setStatus(request.status());

        return faturaRepository.save(fatura);
    }

    @Transactional
    public Fatura salvar(Fatura fatura) {
        if (fatura.getAluno() == null) {
            throw new IllegalArgumentException("Uma fatura deve ter um aluno");
        }
        return faturaRepository.save(fatura);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas admin pode deletar faturas");
        }

        if (!faturaRepository.existsById(id)) {
            throw new RuntimeException("Não existe fatura com ID: " + id);
        }
        faturaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Fatura> buscarPorAlunoId(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !aluno.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar suas próprias faturas");
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar faturas de seus próprios alunos");
            }
        }

        return faturaRepository.findByAluno_UsuarioId(alunoId);
    }

    @Transactional(readOnly = true)
    public List<Fatura> buscarPorStatus(String status) {
        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ADMIN) {
            return faturaRepository.findByStatus(status);
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));

            List<Aluno> alunosDoPersonal = alunoRepository.findByPersonal(personalLogado);

            return faturaRepository.findByAlunoInAndStatus(alunosDoPersonal, status);
        }

        throw new RuntimeException("Acesso negado");
    }
}