package br.edu.ufape.personal_trainer.config;

import br.edu.ufape.personal_trainer.model.*;
import br.edu.ufape.personal_trainer.repository.*;
import br.edu.ufape.personal_trainer.security.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            PersonalRepository personalRepo,
            AlunoRepository alunoRepo,
            GrupoMuscularRepository grupoRepo,
            ExercicioRepository exercicioRepo,
            PlanoDeTreinoRepository planoRepo,
            ItemTreinoRepository itemRepo,
            FaturaRepository faturaRepo,
            AvaliacaoFisicaRepository avaliacaoRepo,
            ChatRepository chatRepo,
            MensagemRepository mensagemRepo,
            PasswordEncoder encoder
    ) {
        return args -> {

            // personais
            if (personalRepo.findByEmail("profmarcos@email.com").isEmpty()) {
                Personal marcos = new Personal();
                marcos.setNome("Prof. Marcos Silva");
                marcos.setEmail("profmarcos@email.com");
                marcos.setSenha(encoder.encode("123456"));
                marcos.setCref("123456-SP");
                marcos.setRole(Role.PERSONAL);
                personalRepo.save(marcos);
            }

            if (personalRepo.findByEmail("anacosta@email.com").isEmpty()) {
                Personal ana = new Personal();
                ana.setNome("Profa. Ana Costa");
                ana.setEmail("anacosta@email.com");
                ana.setSenha(encoder.encode("123456"));
                ana.setCref("654321-RJ");
                ana.setRole(Role.PERSONAL);
                personalRepo.save(ana);
            }

            Personal marcos = personalRepo.findByEmail("profmarcos@email.com").get();
            Personal ana = personalRepo.findByEmail("anacosta@email.com").get();

            // alunos
            if (alunoRepo.findByEmail("joao@email.com").isEmpty()) {
                Aluno joao = new Aluno();
                joao.setNome("João Silva");
                joao.setEmail("joao@email.com");
                joao.setSenha(encoder.encode("123456"));
                joao.setDataNascimento(LocalDate.of(1998, 5, 15));
                joao.setModalidade("presencial");
                joao.setObjetivo("hipertrofia");
                joao.setAtivo(true);
                joao.setPersonal(marcos);
                joao.setRole(Role.ALUNO);
                alunoRepo.save(joao);
            }

            if (alunoRepo.findByEmail("maria@email.com").isEmpty()) {
                Aluno maria = new Aluno();
                maria.setNome("Maria Santos");
                maria.setEmail("maria@email.com");
                maria.setSenha(encoder.encode("123456"));
                maria.setDataNascimento(LocalDate.of(2000, 3, 22));
                maria.setModalidade("online");
                maria.setObjetivo("emagrecimento");
                maria.setAtivo(true);
                maria.setPersonal(ana);
                maria.setRole(Role.ALUNO);
                alunoRepo.save(maria);
            }

            if (alunoRepo.findByEmail("pedro@email.com").isEmpty()) {
                Aluno pedro = new Aluno();
                pedro.setNome("Pedro Almeida");
                pedro.setEmail("pedro@email.com");
                pedro.setSenha(encoder.encode("123456"));
                pedro.setDataNascimento(LocalDate.of(1995, 11, 10));
                pedro.setModalidade("presencial");
                pedro.setObjetivo("hipertrofia");
                pedro.setAtivo(true);
                pedro.setPersonal(marcos);
                pedro.setRole(Role.ALUNO);
                alunoRepo.save(pedro);
            }

            if (alunoRepo.findByEmail("camila@email.com").isEmpty()) {
                Aluno camila = new Aluno();
                camila.setNome("Camila Oliveira");
                camila.setEmail("camila@email.com");
                camila.setSenha(encoder.encode("123456"));
                camila.setDataNascimento(LocalDate.of(2002, 7, 30));
                camila.setModalidade("online");
                camila.setObjetivo("condicionamento");
                camila.setAtivo(true);
                camila.setPersonal(ana);
                camila.setRole(Role.ALUNO);
                alunoRepo.save(camila);
            }

            // grupos musculares
            if (grupoRepo.findByNome("Peito").isEmpty()) {
                GrupoMuscular peito = new GrupoMuscular();
                peito.setNome("Peito");
                peito.setDescricao("Músculos peitorais maior e menor");
                grupoRepo.save(peito);
            }
            if (grupoRepo.findByNome("Costas").isEmpty()) {
                GrupoMuscular costas = new GrupoMuscular();
                costas.setNome("Costas");
                costas.setDescricao("Dorsais, trapézio, romboides");
                grupoRepo.save(costas);
            }
            if (grupoRepo.findByNome("Ombros").isEmpty()) {
                GrupoMuscular ombros = new GrupoMuscular();
                ombros.setNome("Ombros");
                ombros.setDescricao("Deltoides anterior, lateral e posterior");
                grupoRepo.save(ombros);
            }
            if (grupoRepo.findByNome("Bíceps").isEmpty()) {
                GrupoMuscular biceps = new GrupoMuscular();
                biceps.setNome("Bíceps");
                biceps.setDescricao("Bíceps braquial");
                grupoRepo.save(biceps);
            }
            if (grupoRepo.findByNome("Tríceps").isEmpty()) {
                GrupoMuscular triceps = new GrupoMuscular();
                triceps.setNome("Tríceps");
                triceps.setDescricao("Tríceps braquial");
                grupoRepo.save(triceps);
            }
            if (grupoRepo.findByNome("Pernas").isEmpty()) {
                GrupoMuscular pernas = new GrupoMuscular();
                pernas.setNome("Pernas");
                pernas.setDescricao("Quadríceps, posteriores, panturrilhas");
                grupoRepo.save(pernas);
            }
            if (grupoRepo.findByNome("Abdômen").isEmpty()) {
                GrupoMuscular abdomen = new GrupoMuscular();
                abdomen.setNome("Abdômen");
                abdomen.setDescricao("Reto abdominal, oblíquos");
                grupoRepo.save(abdomen);
            }

            // exercicios
            GrupoMuscular peito = grupoRepo.findByNome("Peito").get();
            GrupoMuscular costas = grupoRepo.findByNome("Costas").get();
            GrupoMuscular ombros = grupoRepo.findByNome("Ombros").get();
            GrupoMuscular pernas = grupoRepo.findByNome("Pernas").get();
            GrupoMuscular abdomen = grupoRepo.findByNome("Abdômen").get();

            if (exercicioRepo.findByNomeContainingIgnoreCase("Supino reto").isEmpty()) {
                Exercicio e = new Exercicio();
                e.setNome("Supino reto com barra");
                e.setDescricao("Exercício composto para peitoral");
                e.setGrupoMuscular(peito);
                exercicioRepo.save(e);
            }
            if (exercicioRepo.findByNomeContainingIgnoreCase("Supino inclinado").isEmpty()) {
                Exercicio e = new Exercicio();
                e.setNome("Supino inclinado com halteres");
                e.setDescricao("Peitoral superior");
                e.setGrupoMuscular(peito);
                exercicioRepo.save(e);
            }
            if (exercicioRepo.findByNomeContainingIgnoreCase("Puxada frontal").isEmpty()) {
                Exercicio e = new Exercicio();
                e.setNome("Puxada frontal");
                e.setDescricao("Dorsais");
                e.setGrupoMuscular(costas);
                exercicioRepo.save(e);
            }
            if (exercicioRepo.findByNomeContainingIgnoreCase("Remada curvada").isEmpty()) {
                Exercicio e = new Exercicio();
                e.setNome("Remada curvada");
                e.setDescricao("Costas médias");
                e.setGrupoMuscular(costas);
                exercicioRepo.save(e);
            }
            if (exercicioRepo.findByNomeContainingIgnoreCase("Desenvolvimento").isEmpty()) {
                Exercicio e = new Exercicio();
                e.setNome("Desenvolvimento com barra");
                e.setDescricao("Deltoides");
                e.setGrupoMuscular(ombros);
                exercicioRepo.save(e);
            }
            if (exercicioRepo.findByNomeContainingIgnoreCase("Agachamento").isEmpty()) {
                Exercicio e = new Exercicio();
                e.setNome("Agachamento livre");
                e.setDescricao("Quadríceps e glúteos");
                e.setGrupoMuscular(pernas);
                exercicioRepo.save(e);
            }
            if (exercicioRepo.findByNomeContainingIgnoreCase("Prancha").isEmpty()) {
                Exercicio e = new Exercicio();
                e.setNome("Prancha abdominal");
                e.setDescricao("Core isométrico");
                e.setGrupoMuscular(abdomen);
                exercicioRepo.save(e);
            }

            // interacoes personal marcos e aluno joao

            Aluno joao = alunoRepo.findByEmail("joao@email.com").get();

            // plano de treino
            if (planoRepo.findByAluno_UsuarioIdAndAtivoTrue(joao.getUsuarioId()).isEmpty()) {
                PlanoDeTreino plano = new PlanoDeTreino();
                plano.setNome("Plano Hipertrofia ABC");
                plano.setDuracaoSemanas(12);
                plano.setDataInicio(LocalDate.of(2025, 12, 1));
                plano.setDataFim(LocalDate.of(2026, 2, 22));
                plano.setAtivo(true);
                plano.setAluno(joao);
                plano = planoRepo.save(plano);

                // 3 itens de treino
                ItemTreino item1 = new ItemTreino();
                item1.setSeries(4);
                item1.setRepeticoes("8-12");
                item1.setCargaKg(80.0);
                item1.setDescansoSegundos(90);
                item1.setPlano(plano);
                item1.setExercicio(exercicioRepo.findByNomeContainingIgnoreCase("Supino reto").get(0));
                itemRepo.save(item1);

                ItemTreino item2 = new ItemTreino();
                item2.setSeries(4);
                item2.setRepeticoes("10-12");
                item2.setCargaKg(60.0);
                item2.setDescansoSegundos(60);
                item2.setPlano(plano);
                item2.setExercicio(exercicioRepo.findByNomeContainingIgnoreCase("Puxada frontal").get(0));
                itemRepo.save(item2);

                ItemTreino item3 = new ItemTreino();
                item3.setSeries(4);
                item3.setRepeticoes("8-10");
                item3.setCargaKg(100.0);
                item3.setDescansoSegundos(120);
                item3.setPlano(plano);
                item3.setExercicio(exercicioRepo.findByNomeContainingIgnoreCase("Agachamento").get(0));
                itemRepo.save(item3);
            }

            // fatura
            if (faturaRepo.findByAluno_UsuarioIdAndStatus(joao.getUsuarioId(), "PENDENTE").isEmpty()) {
                Fatura fatura = new Fatura();
                fatura.setValor(250.0);
                fatura.setDataVencimento(LocalDate.of(2025, 12, 20));
                fatura.setStatus("PENDENTE");
                fatura.setAluno(joao);
                faturaRepo.save(fatura);
            }

            // avaliacao fisica
            if (avaliacaoRepo.findByAlunoUsuarioId(joao.getUsuarioId()).isEmpty()) {
                AvaliacaoFisica av = new AvaliacaoFisica();
                av.setDataAvaliacao(LocalDate.of(2025, 12, 10));
                av.setPesoKg(85.5);
                av.setAlturaCm(178.0);
                av.setPercentualGordura(18.5);
                av.setObservacoes("Início do plano de hipertrofia");
                av.setFeitoPeloPersonal(true);
                av.setAluno(joao);
                avaliacaoRepo.save(av);
            }

            // chat + 2 mensagens
            Chat chat = chatRepo.findByAluno_UsuarioIdAndPersonal_UsuarioId(joao.getUsuarioId(), marcos.getUsuarioId())
                    .orElseGet(() -> {
                        Chat c = new Chat();
                        c.setAluno(joao);
                        c.setPersonal(marcos);
                        return chatRepo.save(c);
                    });

            if (mensagemRepo.findByChat_ChatIdOrderByTimeStamp(chat.getChatId()).isEmpty()) {
                Mensagem msgAluno = new Mensagem();
                msgAluno.setConteudo("Professor, posso aumentar a carga no supino?");
                msgAluno.setEnviadoPeloAluno(true);
                msgAluno.setLida(false);
                msgAluno.setTimeStamp(LocalDateTime.now());
                msgAluno.setChat(chat);
                mensagemRepo.save(msgAluno);

                Mensagem msgPersonal = new Mensagem();
                msgPersonal.setConteudo("Pode sim, João! Suba 5kg se a técnica estiver boa.");
                msgPersonal.setEnviadoPeloAluno(false);
                msgPersonal.setLida(true);
                msgPersonal.setTimeStamp(LocalDateTime.now().plusMinutes(10));
                msgPersonal.setChat(chat);
                mensagemRepo.save(msgPersonal);
            }
        };
    }
}