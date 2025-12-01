package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.Fatura;
import br.edu.ufape.personal_trainer.repository.FaturaRepository;

@Service
public class FaturaService {

	@Autowired
	private FaturaRepository faturaRepository;
	
	// listar todos
	public List<Fatura> listarTodos(){
		return faturaRepository.findAll();
	}
	
	// buscar id
	public Fatura buscarId(Long id) {
		return faturaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe fatura com ID: " + id));
	}
	
	// salvar (MUITO PROVAVELMENTE ADICIONAR MAIS)
	public Fatura salvar(Fatura fatura) {
		if(fatura.getAluno() == null) {
			throw new IllegalArgumentException("Uma fatura deve ter um aluno");
		}
		return faturaRepository.save(fatura);
	}
	
	// deletar
	public void deletar(Long id) {
		if(!faturaRepository.existsById(id)) {
			throw new RuntimeException("Não existe fatura com ID: " + id);
		}
		faturaRepository.deleteById(id);
	}
	
	// metodos personalizados
	public List<Fatura> buscarPorAlunoId(Long alunoId){
		return faturaRepository.findByAluno_IdUsuario(alunoId);
	}
	
	public List<Fatura> buscarPorStatus(String status){
		return faturaRepository.findByStatus(status);
	}
}
