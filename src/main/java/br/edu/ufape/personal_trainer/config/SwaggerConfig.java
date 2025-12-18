package br.edu.ufape.personal_trainer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Personal Trainer API - UFAPE 2025")
                        .version("1.0")
                        .description("""
                                <h2>Como autenticar</h2>
                                <p>Esta API usa <strong>Basic Auth</strong>.</p>
                                <ol>
                                    <li>Clique no botão verde <strong>Authorize</strong> abaixo</li>
                                    <li>Username → e-mail (ex: joao@email.com)</li>
                                    <li>Password → 123456 (ou admin123 para admin)</li>
                                    <li>Clique em <strong>Authorize</strong></li>
                                </ol>
                                <p><strong>Sem isso, todos os endpoints retornam 401/403!</strong></p>
                                """))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .name("basicAuth")
                                .description("Basic Authentication (e-mail + senha)")));
    }
}