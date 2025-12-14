package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.AlunoRequest;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarTodos() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return alunoRepository.findAll();
        }
        if (logado.getRole() == Role.PERSONAL) {
            Personal personal = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            return personal.getAlunos();
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public Aluno buscarId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !aluno.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar seus próprios dados");
        }

        if (logado.getRole() == Role.PERSONAL) {
            Personal personal = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(personal.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar dados de seus próprios alunos");
            }
        }

        return aluno;
    }

    @Transactional
    public Aluno criar(AlunoRequest request) {
        Aluno aluno = new Aluno();
        aluno.setNome(request.nome());
        aluno.setEmail(request.email());
        aluno.setSenha(passwordEncoder.encode(request.senha()));
        aluno.setDataNascimento(request.dataNascimento());
        aluno.setModalidade(request.modalidade());
        aluno.setObjetivo(request.objetivo());
        aluno.setAtivo(false);
        aluno.setRole(Role.ALUNO);
        return alunoRepository.save(aluno);
    }

    @Transactional
    public Aluno salvar(Aluno aluno) {
        if (aluno.getEmail() == null || aluno.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        return alunoRepository.save(aluno);
    }

    @Transactional
    public void deletar(Long id) {
        Aluno aluno = buscarId(id);
        if (!aluno.getFaturas().isEmpty()) {
            throw new IllegalStateException("Aluno possui faturas — não pode ser deletado");
        }
        if (!aluno.getPlanos().isEmpty()) {
            throw new IllegalStateException("Aluno possui planos de treino — não pode ser deletado");
        }
        alunoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarPorModalidade(String modalidade) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return alunoRepository.findByModalidade(modalidade);
        }
        if (logado.getRole() == Role.PERSONAL) {
            Personal personal = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            return alunoRepository.findByPersonalAndModalidade(personal, modalidade);
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarPorAtivo() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return alunoRepository.findByAtivoTrue();
        }
        if (logado.getRole() == Role.PERSONAL) {
            Personal personal = personalRepository.findById(logado.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Personal não encontrado"));
            return personal.getAlunos().stream()
                    .filter(Aluno::getAtivo)
                    .toList();
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public Aluno buscarEmail(String email) {
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com email: " + email));

        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ALUNO && !aluno.getEmail().equals(logado.getEmail())) {
            throw new RuntimeException("Você só pode buscar seus próprios dados");
        }
        if (logado.getRole() == Role.PERSONAL) {
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
                throw new RuntimeException("Você só pode buscar dados de seus próprios alunos");
            }
        }
        return aluno;
    }

    @Transactional
    public void VincularPersonal(Long alunoId, Long personalId) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.PERSONAL || !logado.getUsuarioId().equals(personalId)) {
            throw new RuntimeException("Você só pode vincular alunos a si mesmo");
        }

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Personal personal = personalRepository.findById(personalId)
                .orElseThrow(() -> new RuntimeException("Personal não encontrado"));

        if (aluno.getPersonal() != null) {
            throw new IllegalArgumentException("Aluno já está vinculado a um personal");
        }

        aluno.setPersonal(personal);
        aluno.setAtivo(true);
        alunoRepository.save(aluno);
    }

    @Transactional
    public void DesvincularPersonal(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = auth.getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado)
            .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));

        if (usuarioLogado.getRole() == Role.PERSONAL) {
            if (aluno.getPersonal() == null) {
                throw new RuntimeException("Aluno não está vinculado a nenhum personal");
            }
            if (!aluno.getPersonal().getUsuarioId().equals(usuarioLogado.getUsuarioId())) {
                throw new AccessDeniedException("Você só pode desvincular seus próprios alunos");
            }
        }

        aluno.setPersonal(null);
        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }


}