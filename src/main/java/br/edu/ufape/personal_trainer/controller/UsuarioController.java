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

import br.edu.ufape.personal_trainer.model.Usuario;
import br.edu.ufape.personal_trainer.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Usuario buscarId(@PathVariable Long id) {
        return usuarioService.buscarId(id);
    }

    @PostMapping
    public Usuario salvar(@RequestBody Usuario usuario) {
        return usuarioService.salvar(usuario);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }

    @GetMapping("/nome")
    public List<Usuario> buscarPorNome(@RequestParam String nome) {
        return usuarioService.buscarPorNome(nome);
    }

    @GetMapping("/email/{email}")
    public Usuario buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email);
    }
}