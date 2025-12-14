package br.edu.ufape.personal_trainer.config;

import br.edu.ufape.personal_trainer.model.Admin;
import br.edu.ufape.personal_trainer.repository.UsuarioRepository;
import br.edu.ufape.personal_trainer.security.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
        return args -> {

            if (repo.findByEmail("admin@admin.com").isEmpty()) {

                Admin admin = new Admin();
                admin.setNome("Administrador");
                admin.setEmail("admin@admin.com");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                repo.save(admin);
            }
        };
    }
}
