package az.hamburg.it.hamburg.academy.website.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Hamburg Academy API", version = "v1"),
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
        name = "BearerAuth",
        type = HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = HEADER
)
public class SwaggerConfig {
}
