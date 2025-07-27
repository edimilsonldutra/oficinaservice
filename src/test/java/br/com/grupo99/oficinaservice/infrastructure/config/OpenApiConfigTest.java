package br.com.grupo99.oficinaservice.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Teste de Configuração - OpenApiConfig")
class OpenApiConfigTest {

    @Test
    @DisplayName("Deve carregar o contexto da aplicação com a configuração do OpenAPI")
    void contextLoads() {
        // Este teste apenas verifica se a aplicação consegue arrancar
        // com a classe de configuração presente, validando que não há erros de bean.
    }
}
