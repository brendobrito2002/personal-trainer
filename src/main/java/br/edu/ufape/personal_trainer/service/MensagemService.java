package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.Mensagem;
import br.edu.ufape.personal_trainer.repository.MensagemRepository;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    // listar todos
    public List<Mensagem> listarTodos() {
        return mensagemRepository.findAll();
    }

    // buscar id
    public Mensagem buscarId(Long id) {
        return mensagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe mensagem com ID: " + id));
    }

    // salvar (MUITO PROVAVELMENTE ADICIONAR MAIS)
    public Mensagem salvar(Mensagem mensagem) {
        if (mensagem.getChat() == null) {
            throw new IllegalArgumentException("Mensagem deve pertencer a um chat");
        }
        return mensagemRepository.save(mensagem);
    }

    // deletar
    public void deletar(Long id) {
        if (!mensagemRepository.existsById(id)) {
            throw new RuntimeException("Não existe mensagem com ID: " + id);
        }
        mensagemRepository.deleteById(id);
    }

    // métodos personalizados
    public List<Mensagem> buscarPorChatId(Long chatId) {
        return mensagemRepository.findByChat_ChatIdOrderByTimeStamp(chatId);
    }

    public List<Mensagem> buscarEnviadasPeloAluno(Long chatId) {
        return mensagemRepository.findByChat_ChatIdAndEnviadoPeloAlunoTrue(chatId);
    }

    public List<Mensagem> buscarEnviadasPeloPersonal(Long chatId) {
        return mensagemRepository.findByChat_ChatIdAndEnviadoPeloAlunoFalse(chatId);
    }
}