package py.edu.uc.lp3.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Estado", description = "Comprobación de disponibilidad del servicio")
public class IndexController {

	@GetMapping("/")
	@Operation(summary = "Comprueba que la API está activa")
	public String index() {
		return "API Minecraft - SGDFM";
	}
}
