package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.model.GrupoMuscular;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.GrupoMuscularRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class GrupoMuscularService {

    @Autowired
    private GrupoMuscularRepository grupoMuscularRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<GrupoMuscular> listarTodos() {
        return grupoMuscularRepository.findAll();
    }

    @Transactional(readOnly = true)
    public GrupoMuscular buscarId(Long id) {
        return grupoMuscularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe grupo muscular com ID: " + id));
    }

    @Transactional
    public GrupoMuscular salvar(GrupoMuscular grupoMuscular) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.PERSONAL && logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas personal ou admin pode criar/editar grupos musculares");
        }

        if (grupoMuscular.getNome() == null || grupoMuscular.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Um grupo muscular deve ter um nome");
        }
        if (grupoMuscularRepository.findByNome(grupoMuscular.getNome()).isPresent()) {
            throw new IllegalArgumentException("Já existe grupo muscular com nome: " + grupoMuscular.getNome());
        }
        return grupoMuscularRepository.save(grupoMuscular);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas admin pode deletar grupos musculares");
        }

        if (!grupoMuscularRepository.existsById(id)) {
            throw new RuntimeException("Não existe grupo muscular com ID: " + id);
        }
        grupoMuscularRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public GrupoMuscular buscarPorNome(String nome) {
        return grupoMuscularRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Não existe grupo muscular com NOME: " + nome));
    }
}