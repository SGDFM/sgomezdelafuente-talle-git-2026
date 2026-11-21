package py.edu.uc.lp3.rest.controller;

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
public class SimulacionController {

	private final ExplosionService service;
	private final JuegoService juegoService;

	public SimulacionController(ExplosionService service, JuegoService juegoService) {
		this.service = service;
		this.juegoService = juegoService;
	}

	@PostMapping("/explosion")
	public ExplosionResponse simularExplosion(@RequestBody ExplosionRequest request) {
		return ExplosionResponse.desde(
				service.simular(request.creeperId(), request.jugadorId(), request.zombieId(), request.radio()),
				juegoService);
	}
}
