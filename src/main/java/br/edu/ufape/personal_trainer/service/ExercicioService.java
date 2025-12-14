package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.ExercicioRequest;
import br.edu.ufape.personal_trainer.model.Exercicio;
import br.edu.ufape.personal_trainer.model.GrupoMuscular;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.ExercicioRepository;
import br.edu.ufape.personal_trainer.repository.GrupoMuscularRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class ExercicioService {

    @Autowired
    private ExercicioRepository exercicioRepository;

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
    public List<Exercicio> listarTodos() {
        return exercicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Exercicio buscarId(Long id) {
        return exercicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe exercício com ID: " + id));
    }

    @Transactional
    public Exercicio criar(ExercicioRequest request) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.PERSONAL && logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas personal ou admin pode criar exercícios");
        }

        GrupoMuscular grupoMuscular = grupoMuscularRepository.findById(request.grupoMuscularId())
                .orElseThrow(() -> new RuntimeException("Grupo muscular não encontrado"));

        if (!exercicioRepository.findByNomeContainingIgnoreCase(request.nome()).isEmpty()) {
            throw new IllegalArgumentException("Já existe um exercício com nome semelhante: " + request.nome());
        }

        Exercicio exercicio = new Exercicio();
        exercicio.setNome(request.nome());
        exercicio.setDescricao(request.descricao());
        exercicio.setGrupoMuscular(grupoMuscular);

        return exercicioRepository.save(exercicio);
    }

    @Transactional
    public Exercicio salvar(Exercicio exercicio) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.PERSONAL && logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas personal ou admin pode editar exercícios");
        }

        if (exercicio.getNome() == null || exercicio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do exercício é obrigatório");
        }
        if (exercicio.getGrupoMuscular() == null) {
            throw new IllegalArgumentException("Grupo muscular é obrigatório");
        }

        return exercicioRepository.save(exercicio);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas admin pode deletar exercícios");
        }

        if (!exercicioRepository.existsById(id)) {
            throw new RuntimeException("Não existe exercício com ID: " + id);
        }
        exercicioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorGrupoMuscular(Long grupoMuscularId) {
        return exercicioRepository.findByGrupoMuscular_GrupoMuscularId(grupoMuscularId);
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorNome(String nome) {
        return exercicioRepository.findByNomeContainingIgnoreCase(nome);
    }
}