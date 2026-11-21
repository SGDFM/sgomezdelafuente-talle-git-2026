package py.edu.uc.lp3.rest.dto;

import py.edu.uc.lp3.service.ExplosionService.ResultadoExplosion;
import py.edu.uc.lp3.service.JuegoService;

public record ExplosionResponse(
		int danoExplosion,
		boolean jugadorAfectado,
		boolean zombieAfectado,
		EntidadResponse creeper,
		EntidadResponse jugador,
		EntidadResponse zombie) {

	public static ExplosionResponse desde(ResultadoExplosion resultado, JuegoService juegoService) {
		return new ExplosionResponse(
				resultado.danoExplosion(),
				resultado.jugadorAfectado(),
				resultado.zombieAfectado(),
				EntidadResponse.desde(resultado.creeper(), juegoService.comportamiento(resultado.creeper())),
				EntidadResponse.desde(resultado.jugador(), juegoService.comportamiento(resultado.jugador())),
				EntidadResponse.desde(resultado.zombie(), juegoService.comportamiento(resultado.zombie())));
	}
}
