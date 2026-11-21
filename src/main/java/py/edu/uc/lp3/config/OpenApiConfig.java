package py.edu.uc.lp3.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI minecraftOpenApi() {
		return new OpenAPI().info(new Info()
				.title("Minecraft API - Sebastián Gómez de la Fuente")
				.version("POO-09")
				.description("CRUD H2, reglas de explosión y escenario cruzado con las APIs de Ruth y Matías.")
				.contact(new Contact().name("Sebastián Gómez de la Fuente").url("https://github.com/SGDFM"))
				.license(new License().name("Apache License 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
	}
}
