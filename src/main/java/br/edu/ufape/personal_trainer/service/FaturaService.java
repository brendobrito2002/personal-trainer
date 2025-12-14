package br.edu.ufape.personal_trainer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufape.personal_trainer.dto.FaturaRequest;
import br.edu.ufape.personal_trainer.model.Aluno;
import br.edu.ufape.personal_trainer.model.Fatura;
import br.edu.ufape.personal_trainer.repository.AlunoRepository;
import br.edu.ufape.personal_trainer.repository.FaturaRepository;

@Service
public class FaturaService {

	@Autowired
	private FaturaRepository faturaRepository;
	
	@Autowired AlunoRepository alunoRepository;
	
	// listar todos
	@Transactional(readOnly = true)
	public List<Fatura> listarTodos(){
		return faturaRepository.findAll();
	}
	
	// buscar id
	@Transactional(readOnly = true)
	public Fatura buscarId(Long id) {
		return faturaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não existe fatura com ID: " + id));
	}
	
	// criar dto
	@Transactional
	public Fatura criar(FaturaRequest request) {
        Aluno aluno = alunoRepository.findById(request.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        if(aluno.getPersonal() == null) {
        	throw new IllegalArgumentException("Aluno precisa estar vinculado a um personal");
        }
        
        if (faturaRepository.findByAluno_UsuarioIdAndStatus(aluno.getUsuarioId(), "PENDENTE").isPresent()) {
            throw new IllegalStateException("Aluno já possui uma fatura pendente");
        }

        Fatura fatura = new Fatura();
        fatura.setAluno(aluno);
        fatura.setValor(request.valor());
        fatura.setDataVencimento(request.dataVencimento());
        fatura.setStatus(request.status());

        return faturaRepository.save(fatura);
    }

	// salvar (MUITO PROVAVELMENTE ADICIONAR MAIS)
	@Transactional
	public Fatura salvar(Fatura fatura) {
		if(fatura.getAluno() == null) {
			throw new IllegalArgumentException("Uma fatura deve ter um aluno");
		}
		return faturaRepository.save(fatura);
	}
	
	// deletar
	@Transactional
	public void deletar(Long id) {
		if(!faturaRepository.existsById(id)) {
			throw new RuntimeException("Não existe fatura com ID: " + id);
		}
		faturaRepository.deleteById(id);
	}
	
	// metodos personalizados
	@Transactional(readOnly = true)
	public List<Fatura> buscarPorAlunoId(Long alunoId){
		return faturaRepository.findByAluno_UsuarioId(alunoId);
	}
	
	@Transactional(readOnly = true)
	public List<Fatura> buscarPorStatus(String status){
		return faturaRepository.findByStatus(status);
	}
}
