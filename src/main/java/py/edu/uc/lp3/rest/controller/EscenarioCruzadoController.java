package py.edu.uc.lp3.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.domain.integration.JugadaCruzada;
import py.edu.uc.lp3.rest.dto.EscenarioExplosionRequest;
import py.edu.uc.lp3.rest.dto.JugadaCruzadaResponse;
import py.edu.uc.lp3.service.EscenarioCruzadoService;

@RestController
@RequestMapping("/minecraft/sgomezdelafuente/escenarios/explosiones")
@Tag(name = "POO-09 - Escenario cruzado", description = "Orquesta las reglas de Ruth (A2) y Matías (A3)")
public class EscenarioCruzadoController {

	private final EscenarioCruzadoService service;

	public EscenarioCruzadoController(EscenarioCruzadoService service) {
		this.service = service;
	}

	@PostMapping
	@Operation(
			summary = "Ejecuta una explosión coordinada",
			description = "Consulta primero la explosión y reacción en cadena de Ruth y luego el radio y ciclo de vida de Matías. Persiste en H2 solamente si ambos aceptan.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "A2 y A3 aceptaron; jugada persistida"),
			@ApiResponse(responseCode = "400", description = "Pedido local inválido"),
			@ApiResponse(responseCode = "422", description = "A2 o A3 rechazó la regla"),
			@ApiResponse(responseCode = "502", description = "A2 o A3 no está disponible")
	})
	public ResponseEntity<JugadaCruzadaResponse> ejecutar(@RequestBody EscenarioExplosionRequest request) {
		JugadaCruzada jugada = service.ejecutar(request);
		return ResponseEntity.created(URI.create("/minecraft/sgomezdelafuente/escenarios/explosiones/" + jugada.getId()))
				.body(JugadaCruzadaResponse.desde(jugada));
	}

	@GetMapping
	@Operation(summary = "Lista las jugadas cruzadas aceptadas y persistidas")
	public List<JugadaCruzadaResponse> listar() {
		return service.listar().stream().map(JugadaCruzadaResponse::desde).toList();
	}
}
