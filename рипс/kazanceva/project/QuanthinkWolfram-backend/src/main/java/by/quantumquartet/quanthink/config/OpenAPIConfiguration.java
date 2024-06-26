package by.quantumquartet.quanthink.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.description("Quanthink Wolfram");

        Info information = new Info()
                .title("Quanthink Wolfram API")
                .version("1.0")
                .description("This API provides endpoints for Quanthink Wolfram application.");
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
