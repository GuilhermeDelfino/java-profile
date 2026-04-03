package narciso.guilherme.github.profile.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addRedirectViewController("/docs", "/swagger-ui/index.html");
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("Profile API")
        .description("User profile management")
        .version("1.0.0"))
      .addServersItem(new Server().url("https://api.guilherme-narciso.dev/java-profile").description("Prod"))
      .addServersItem(new Server().url("http://localhost:8080/java-profile").description("Local"))
//        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
      .components(new Components()
        .addSecuritySchemes("bearerAuth", new SecurityScheme()
          .name("bearerAuth")
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")));
  }
}
