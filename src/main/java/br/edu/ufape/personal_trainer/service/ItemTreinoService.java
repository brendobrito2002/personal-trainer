package br.edu.ufape.personal_trainer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.personal_trainer.dto.ItemTreinoRequest;
import br.edu.ufape.personal_trainer.model.Exercicio;
import br.edu.ufape.personal_trainer.model.ItemTreino;
import br.edu.ufape.personal_trainer.model.PlanoDeTreino;
import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.repository.ExercicioRepository;
import br.edu.ufape.personal_trainer.repository.ItemTreinoRepository;
import br.edu.ufape.personal_trainer.repository.PlanoDeTreinoRepository;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;

@Service
public class ItemTreinoService {

    @Autowired
    private ItemTreinoRepository itemTreinoRepository;

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private PlanoDeTreinoRepository planoDeTreinoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ItemTreino> listarTodos() {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN || logado.getRole() == Role.PERSONAL || logado.getRole() == Role.ALUNO) {
            return itemTreinoRepository.findAll();
        }
        throw new RuntimeException("Acesso negado");
    }

    @Transactional(readOnly = true)
    public ItemTreino buscarId(Long id) {
        ItemTreino item = itemTreinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de treino não encontrado"));

        PlanoDeTreino plano = item.getPlano();
        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !plano.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar itens do seu próprio plano");
        }
        if (logado.getRole() == Role.PERSONAL) {
            if (plano.getAluno().getPersonal() == null || !plano.getAluno().getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar itens de planos dos seus alunos");
            }
        }

        return item;
    }

    @Transactional
    public ItemTreino criar(ItemTreinoRequest request, Long planoId) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.PERSONAL && logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas personal ou admin pode criar itens de treino");
        }

        PlanoDeTreino plano = planoDeTreinoRepository.findById(planoId)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));

        if (logado.getRole() == Role.PERSONAL) {
            if (plano.getAluno().getPersonal() == null || !plano.getAluno().getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
                throw new RuntimeException("Você só pode adicionar itens aos planos dos seus alunos");
            }
        }

        Exercicio exercicio = exercicioRepository.findById(request.exercicioId())
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        ItemTreino itemTreino = new ItemTreino();
        itemTreino.setExercicio(exercicio);
        itemTreino.setPlano(plano);
        itemTreino.setSeries(request.series());
        itemTreino.setRepeticoes(request.repeticoes());
        itemTreino.setCargaKg(request.cargaKg());
        itemTreino.setDescansoSegundos(request.descansoSegundos());

        return itemTreinoRepository.save(itemTreino);
    }

    @Transactional
    public ItemTreino salvar(ItemTreino itemTreino) {
        if (itemTreino.getPlano() == null) {
            throw new IllegalArgumentException("ItemTreino deve ter um plano");
        }
        if (itemTreino.getExercicio() == null) {
            throw new IllegalArgumentException("ItemTreino deve ter um exercício");
        }
        return itemTreinoRepository.save(itemTreino);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() != Role.ADMIN) {
            throw new RuntimeException("Apenas admin pode deletar itens de treino");
        }

        if (!itemTreinoRepository.existsById(id)) {
            throw new RuntimeException("Não existe item treino com ID: " + id);
        }
        itemTreinoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ItemTreino> buscarPorPlanoId(Long planoId) {
        PlanoDeTreino plano = planoDeTreinoRepository.findById(planoId)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));

        Usuario logado = getUsuarioLogado();

        if (logado.getRole() == Role.ALUNO && !plano.getAluno().getUsuarioId().equals(logado.getUsuarioId())) {
            throw new RuntimeException("Você só pode visualizar itens do seu próprio plano");
        }
        if (logado.getRole() == Role.PERSONAL) {
            if (plano.getAluno().getPersonal() == null || !plano.getAluno().getPersonal().getUsuarioId().equals(logado.getUsuarioId())) {
                throw new RuntimeException("Você só pode visualizar itens de planos dos seus alunos");
            }
        }

        return itemTreinoRepository.findByPlano_PlanoId(planoId);
    }

    @Transactional(readOnly = true)
    public List<ItemTreino> buscarPorExercicioId(Long exercicioId) {
        Usuario logado = getUsuarioLogado();
        if (logado.getRole() == Role.ADMIN || logado.getRole() == Role.PERSONAL || logado.getRole() == Role.ALUNO) {
            return itemTreinoRepository.findByExercicio_ExercicioId(exercicioId);
        }
        throw new RuntimeException("Acesso negado");
    }
}