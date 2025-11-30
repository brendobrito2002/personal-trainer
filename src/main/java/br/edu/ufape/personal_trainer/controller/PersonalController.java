package br.edu.ufape.personal_trainer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.personal_trainer.model.Personal;
import br.edu.ufape.personal_trainer.service.PersonalService;

@RestController
@RequestMapping("/api/personais")
public class PersonalController {

	@Autowired
	private PersonalService personalService;
	
	@GetMapping
	public List<Personal> listarTodos(){
		return personalService.listarTodos();
	}
	
	@GetMapping("/{id}")
	public Personal buscarId(@PathVariable Long id) {
		return personalService.buscarId(id);
	}
	
	@PostMapping
	public Personal salvar(@RequestBody Personal personal) {
		return personalService.salvar(personal);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		personalService.deletar(id);
	}
	
	@GetMapping("/cref/{cref}")
	public Personal buscarPorCref(@PathVariable String cref) {
		return personalService.buscarPorCref(cref);
	}
	
	@GetMapping("/email/{email}")
	public Personal buscarPorEmail(@PathVariable String email) {
		return personalService.buscarPorEmail(email);
	}
}
