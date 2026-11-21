package py.edu.uc.lp3.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.rest.dto.ExplosionRequest;
import py.edu.uc.lp3.rest.dto.ExplosionResponse;
import py.edu.uc.lp3.service.ExplosionService;
import py.edu.uc.lp3.service.JuegoService;

@RestController
@RequestMapping("/minecraft/sgomezdelafuente/simulaciones")
@Tag(name = "Simulaciones", description = "Reglas persistidas de explosión de Minecraft")
public class SimulacionController {

	private final ExplosionService service;
	private final JuegoService juegoService;

	public SimulacionController(ExplosionService service, JuegoService juegoService) {
		this.service = service;
		this.juegoService = juegoService;
	}

	@PostMapping("/explosion")
	@Operation(
			summary = "Simula una explosión",
			description = "Exige un Creeper vivo con carga mínima 3. Aplica daño por distancia y reduce el daño del Jugador con su armadura. Responde 404, 400 o 409 según la regla incumplida.")
	public ExplosionResponse simularExplosion(@RequestBody ExplosionRequest request) {
		return ExplosionResponse.desde(
				service.simular(request.creeperId(), request.jugadorId(), request.zombieId(), request.radio()),
				juegoService);
	}
}
