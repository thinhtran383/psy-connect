package online.thinhtran.psyconnect.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(

        servers = {
                @Server(url = "http://localhost:8080", description = "Server URL cho phát triển"),
                @Server(url = "https://api.thinhtran.online", description = "Server URL cho production")
        }
)
@Configuration
public class SwaggerConfig {
//    private SecurityScheme createAPIKeyScheme() {
//        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
//                .bearerFormat("JWT")
//                .scheme("bearer");
//    }
//
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI().addSecurityItem(new SecurityRequirement().
//                        addList("Bearer Authentication"))
//                .components(new Components().addSecuritySchemes
//                        ("Bearer Authentication", createAPIKeyScheme()));
//    }
}