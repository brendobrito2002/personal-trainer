package br.edu.ufape.personal_trainer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.personal_trainer.model.PlanoDeTreino;
import br.edu.ufape.personal_trainer.service.PlanoDeTreinoService;

@RestController
@RequestMapping("/api/planos")
public class PlanoDeTreinoController {

    @Autowired
    private PlanoDeTreinoService planoDeTreinoService;

    @GetMapping
    public List<PlanoDeTreino> listarTodos() {
        return planoDeTreinoService.listarTodos();
    }

    @GetMapping("/{id}")
    public PlanoDeTreino buscarId(@PathVariable Long id) {
        return planoDeTreinoService.buscarId(id);
    }

    @PostMapping
    public PlanoDeTreino salvar(@RequestBody PlanoDeTreino plano) {
        return planoDeTreinoService.salvar(plano);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        planoDeTreinoService.deletar(id);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<PlanoDeTreino> buscarPorAlunoId(@PathVariable Long alunoId) {
        return planoDeTreinoService.buscarPorAlunoId(alunoId);
    }

    @GetMapping("/nome")
    public List<PlanoDeTreino> buscarPorNome(@RequestParam String nome) {
        return planoDeTreinoService.buscarPorNome(nome);
    }
}