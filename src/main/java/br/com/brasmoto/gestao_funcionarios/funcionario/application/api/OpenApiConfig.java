package br.com.brasmoto.gestao_funcionarios.funcionario.application.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestaoFuncionariosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestao de Funcionarios")
                        .version("v1")
                        .description("API REST para gestao de funcionarios")
                        .contact(new Contact()
                                .name("Everton")
                                .url("https://github.com/Irinius777")));
    }
}
