package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.ChatRequest;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Chat;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
import br.edu.ufape.personal_trainer.repository.ChatRepository;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

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
    public List<Chat> listarTodos() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return chatRepository.findAll();
        }
        if (logado.getRole() == Role.PERSONAL) {
            return chatRepository.findByPersonal_UsuarioId(logado.getUsuarioId());
        }
        if (logado.getRole() == Role.ALUNO) {
            return chatRepository.findByAluno_UsuarioId(logado.getUsuarioId());
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public Chat buscarId(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !chat.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar seu próprio chat");
        }
        if (logado.getRole() == Role.PERSONAL && !chat.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar chats com seus alunos");
        }

        return chat;
    }

    @Transactional
    public Chat criar(ChatRequest request) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.PERSONAL || !logado.getUsuarioId().equals(request.personalId())) {
            throw new RuntimeException("Você só pode criar chat para si mesmo");
        }

        if (chatRepository.findByAluno_UsuarioIdAndPersonal_UsuarioId(
                request.alunoId(), request.personalId()).isPresent()) {
            throw new IllegalArgumentException("Chat já existe entre este aluno e personal");
        }

        Aluno aluno = alunoRepository.findById(request.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Aluno não está vinculado a você");
        }

        Personal personal = personalRepository.findById(request.personalId())
                .orElseThrow(() -> new RuntimeException("Personal não encontrado"));

        Chat chat = new Chat();
        chat.setAluno(aluno);
        chat.setPersonal(personal);
        return chatRepository.save(chat);
    }

    @Transactional
    public Chat salvar(Chat chat) {
        if (chat.getAluno() == null || chat.getPersonal() == null) {
            throw new IllegalArgumentException("Um chat deve ter um aluno e um personal");
        }
        Long alunoId = chat.getAluno().getUsuarioId();
        Long personalId = chat.getPersonal().getUsuarioId();
        if (chatRepository.findByAluno_UsuarioIdAndPersonal_UsuarioId(alunoId, personalId).isPresent()) {
            throw new IllegalArgumentException("Chat já existe entre esses usuários");
        }
        return chatRepository.save(chat);
    }

    @Transactional
    public void deletar(Long id) {
        if (!chatRepository.existsById(id)) {
            throw new RuntimeException("Não existe chat com o ID: " + id);
        }
        chatRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Chat> buscarPorAluno(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !aluno.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode buscar seu próprio chat");
        }
        if (logado.getRole() == Role.PERSONAL) {
            if (aluno.getPersonal() == null || !aluno.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
                throw new RuntimeException("Você só pode buscar chats de seus próprios alunos");
            }
        }

        return chatRepository.findByAluno_UsuarioId(alunoId);
    }

    @Transactional(readOnly = true)
    public List<Chat> buscarPorPersonal(Long personalId) {
        Personal personal = personalRepository.findById(personalId)
                .orElseThrow(() -> new RuntimeException("Personal não encontrado"));

        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.PERSONAL && !personal.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode buscar seus próprios chats");
        }

        return chatRepository.findByPersonal_UsuarioId(personalId);
    }

    @Transactional(readOnly = true)
    public Chat buscarPorAlunoIdAndPersonalId(Long alunoId, Long personalId) {
        Chat chat = chatRepository.findByAluno_UsuarioIdAndPersonal_UsuarioId(alunoId, personalId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !chat.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar seu próprio chat");
        }
        if (logado.getRole() == Role.PERSONAL && !chat.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar chats com seus alunos");
        }

        return chat;
    }
}