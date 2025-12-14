package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.AvaliacaoFisicaRequest;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.AvaliacaoFisica;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
import br.edu.ufape.personal_trainer.repository.AvaliacaoFisicaRepository;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class AvaliacaoFisicaService {

    @Autowired
    private AvaliacaoFisicaRepository avaliacaoFisicaRepository;

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
    public List<AvaliacaoFisica> listarTodos() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return avaliacaoFisicaRepository.findAll();
        }
        if (logado.getRole() == Role.PERSONAL) {
            Personal personal = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            List<Aluno> alunosDoPersonal = alunoRepository.findByPersonal(personal);
            return avaliacaoFisicaRepository.findByAlunoIn(alunosDoPersonal);
        }
        if (logado.getRole() == Role.ALUNO) {
            return avaliacaoFisicaRepository.findByAlunoUsuarioId(logado.getUsuarioId());
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public AvaliacaoFisica buscarId(Long id) {
        AvaliacaoFisica av = avaliacaoFisicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação física não encontrada com ID: " + id));

        Usuario logado = getUsuarioLogado();
        Aluno aluno = av.getAluno();

        if (logado.getRole() == Role.ALUNO && !aluno.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar suas próprias avaliações");
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar avaliações de seus próprios alunos");
            }
        }

        return av;
    }

    @Transactional
    public AvaliacaoFisica criar(AvaliacaoFisicaRequest dto, Aluno aluno) {
        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO) {
            if (!aluno.getUsuarioId().equals(logado.getUsuarioId())) {
                throw new RuntimeException("Você só pode criar avaliação para si mesmo");
            }
            if (!"online".equalsIgnoreCase(aluno.getModalidade())) {
                throw new RuntimeException("Apenas alunos com modalidade online podem criar suas próprias avaliações");
            }
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode criar avaliação para seus próprios alunos");
            }
        }

        AvaliacaoFisica av = new AvaliacaoFisica();
        av.setAluno(aluno);
        av.setDataAvaliacao(dto.dataAvaliacao());
        av.setPesoKg(dto.pesoKg());
        av.setAlturaCm(dto.alturaCm());
        av.setPercentualGordura(dto.percentualGordura());
        av.setObservacoes(dto.observacoes());
        av.setFoto(dto.foto());
        av.setFeitoPeloPersonal(logado.getRole() == Role.PERSONAL);

        return avaliacaoFisicaRepository.save(av);
    }

    @Transactional
    public AvaliacaoFisica salvar(AvaliacaoFisica avaliacaoFisica) {
        if (avaliacaoFisica.getAluno() == null) {
            throw new IllegalArgumentException("Aluno é obrigatório");
        }
        return avaliacaoFisicaRepository.save(avaliacaoFisica);
    }

    @Transactional
    public void deletar(Long id) {
        if (!avaliacaoFisicaRepository.existsById(id)) {
            throw new RuntimeException("Não existe Avaliação Física com ID: " + id);
        }
        avaliacaoFisicaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoFisica> encontrarPorIdAluno(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !aluno.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar suas próprias avaliações");
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personalLogado = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personalLogado.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar avaliações de seus próprios alunos");
            }
        }

        return avaliacaoFisicaRepository.findByAlunoUsuarioId(alunoId);
    }
}