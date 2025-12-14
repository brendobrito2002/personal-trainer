package br.edu.ufape.personal_trainer.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.MensagemRequest;
import br.edu.ufape.personal_trainer.model.Chat;
import br.edu.ufape.personal_trainer.model.Mensagem;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.ChatRepository;
import br.edu.ufape.personal_trainer.repository.MensagemRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Mensagem> listarTodos() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return mensagemRepository.findAll();
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public Mensagem buscarId(Long id) {
        Mensagem mensagem = mensagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));

        Chat chat = mensagem.getChat();
        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !chat.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar mensagens do seu próprio chat");
        }
        if (logado.getRole() == Role.PERSONAL && !chat.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar mensagens dos seus próprios chats");
        }

        return mensagem;
    }

    @Transactional
    public Mensagem criar(MensagemRequest request, Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        Usuario logado = getUsuarioLogado();

        boolean enviadoPeloAluno;

        if (logado.getRole() == Role.ALUNO) {
            if (!chat.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
                throw new RuntimeException("Você só pode enviar mensagens no seu próprio chat");
            }
            enviadoPeloAluno = true;
        } else if (logado.getRole() == Role.PERSONAL) {
            if (!chat.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
                throw new RuntimeException("Você só pode enviar mensagens nos chats com seus alunos");
            }
            enviadoPeloAluno = false;
        } else {
            throw new RuntimeException("Acesso negado");
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setConteudo(request.conteudo());
        mensagem.setEnviadoPeloAluno(enviadoPeloAluno);
        mensagem.setLida(false);
        mensagem.setTimeStamp(LocalDateTime.now());
        mensagem.setChat(chat);

        return mensagemRepository.save(mensagem);
    }

    @Transactional
    public Mensagem salvar(Mensagem mensagem) {
        if (mensagem.getChat() == null) {
            throw new IllegalArgumentException("Mensagem deve pertencer a um chat");
        }
        return mensagemRepository.save(mensagem);
    }

    @Transactional
    public void deletar(Long id) {
        if (!mensagemRepository.existsById(id)) {
            throw new RuntimeException("Não existe mensagem com ID: " + id);
        }
        mensagemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Mensagem> buscarPorChatId(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !chat.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar mensagens do seu próprio chat");
        }
        if (logado.getRole() == Role.PERSONAL && !chat.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar mensagens dos seus próprios chats");
        }

        return mensagemRepository.findByChat_ChatIdOrderByTimeStamp(chatId);
    }

    @Transactional(readOnly = true)
    public List<Mensagem> buscarEnviadasPeloAluno(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !chat.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar mensagens do seu próprio chat");
        }
        if (logado.getRole() == Role.PERSONAL && !chat.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar mensagens dos seus próprios chats");
        }

        return mensagemRepository.findByChat_ChatIdAndEnviadoPeloAlunoTrue(chatId);
    }

    @Transactional(readOnly = true)
    public List<Mensagem> buscarEnviadasPeloPersonal(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !chat.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar mensagens do seu próprio chat");
        }
        if (logado.getRole() == Role.PERSONAL && !chat.getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar mensagens dos seus próprios chats");
        }

        return mensagemRepository.findByChat_ChatIdAndEnviadoPeloAlunoFalse(chatId);
    }
}