package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.personal_trainer.model.AvaliacaoFisica;
import br.edu.ufape.personal_trainer.repository.AvaliacaoFisicaRepository;

@Service
public class AvaliacaoFisicaService {

	@Autowired
	private AvaliacaoFisicaRepository avaliacaoFisicaRepository;
	
	// listar todos
	public List<AvaliacaoFisica> listarTodos(){
		return avaliacaoFisicaRepository.findAll();
	}
	
	// buscar id
	public AvaliacaoFisica buscarId(Long id) {
		return avaliacaoFisicaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe uma Avaliação Fisica com o ID: " + id));
	}
	
	// salvar (MUITO PROVAVELMENTE ADICIONAR MAIS)
	public AvaliacaoFisica salvar(AvaliacaoFisica avaliacaoFisica) {
		if(avaliacaoFisica.getAluno() == null) {
			throw new IllegalArgumentException("Aluno é obrigatório");
		}
		return avaliacaoFisicaRepository.save(avaliacaoFisica);
	}
	
	// deletar
	public void deletar(Long id) {
		if(!avaliacaoFisicaRepository.existsById(id)) {
			throw new RuntimeException("Não existe Avaliação Física com ID: " + id);
		}
		avaliacaoFisicaRepository.deleteById(id);
	}
	
	// metodos personalizados
	public List<AvaliacaoFisica> encontrarPorIdAluno(Long id){
		return avaliacaoFisicaRepository.findByAlunoId(id);
	}
}
