package br.edu.ufape.personal_trainer.controller;

import br.edu.ufape.personal_trainer.dto.PersonalRequest;
import br.edu.ufape.personal_trainer.dto.PersonalResponse;
import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.service.PersonalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/personais")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<PersonalResponse> listarTodos() {
        return personalService.listarTodos().stream()
                .map(PersonalResponse::new)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping("/{id}")
    public ResponseEntity<PersonalResponse> buscarId(@PathVariable Long id) {
        Personal personal = personalService.buscarId(id);
        return ResponseEntity.ok(new PersonalResponse(personal));
    }

    @PostMapping
    public ResponseEntity<PersonalResponse> criar(@Valid @RequestBody PersonalRequest request) {
        Personal personal = personalService.criar(request);
        return ResponseEntity.status(201).body(new PersonalResponse(personal));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        personalService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping("/cref/{cref}")
    public ResponseEntity<PersonalResponse> buscarPorCref(@PathVariable String cref) {
        Personal personal = personalService.buscarPorCref(cref);
        return ResponseEntity.ok(new PersonalResponse(personal));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL')")
    @GetMapping("/email/{email}")
    public ResponseEntity<PersonalResponse> buscarPorEmail(@PathVariable String email) {
        Personal personal = personalService.buscarPorEmail(email);
        return ResponseEntity.ok(new PersonalResponse(personal));
    }
}