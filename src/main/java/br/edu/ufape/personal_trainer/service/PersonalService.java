package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.PersonalRequest;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.PersonalRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class PersonalService {

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
    public List<Personal> listarTodos() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN) {
            return personalRepository.findAll();
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public Personal buscarId(Long id) {
        Personal personal = personalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe personal com ID: " + id));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.PERSONAL && !personal.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar seus próprios dados");
        }

        return personal;
    }

    @Transactional
    public Personal criar(PersonalRequest request) {
        if (personalRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (personalRepository.findByCref(request.cref()).isPresent()) {
            throw new IllegalArgumentException("CREF já cadastrado");
        }

        Personal personal = new Personal();
        personal.setNome(request.nome());
        personal.setEmail(request.email());
        personal.setSenha(passwordEncoder.encode(request.senha()));
        personal.setCref(request.cref());
        personal.setRole(Role.PERSONAL);
        return personalRepository.save(personal);
    }

    @Transactional
    public Personal salvar(Personal personal) {
        if (personal.getCref() == null || personal.getCref().trim().isEmpty()) {
            throw new IllegalArgumentException("CREF é obrigatório");
        }
        if (personal.getEmail() == null || personal.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (personalRepository.findByCref(personal.getCref()).isPresent()) {
            throw new IllegalArgumentException("CREF já cadastrado");
        }
        if (personalRepository.findByEmail(personal.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        return personalRepository.save(personal);
    }

    @Transactional
    public void deletar(Long id) {
        Personal personal = buscarId(id);
        if (!personal.getAlunos().isEmpty()) {
            throw new IllegalStateException("Personal possui alunos vinculados — não pode ser deletado");
        }
        personalRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Personal buscarPorCref(String cref) {
        Personal personal = personalRepository.findByCref(cref)
                .orElseThrow(() -> new RuntimeException("Não existe personal com CREF: " + cref));

        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.PERSONAL && !personal.getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode buscar seus próprios dados");
        }

        return personal;
    }

    @Transactional(readOnly = true)
    public Personal buscarPorEmail(String email) {
        Personal personal = personalRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Não existe personal com EMAIL: " + email));

        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.PERSONAL && !personal.getEmail().equals(logado.getEmail())) {
            throw new RuntimeException("Você só pode buscar seus próprios dados");
        }

        return personal;
    }
}