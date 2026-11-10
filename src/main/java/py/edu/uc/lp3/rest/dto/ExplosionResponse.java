package py.edu.uc.lp3.rest.dto;

import py.edu.uc.lp3.service.ExplosionService.ResultadoExplosion;

public record ExplosionResponse(
		int danoExplosion,
		boolean jugadorAfectado,
		boolean zombieAfectado,
		EntidadResponse creeper,
		EntidadResponse jugador,
		EntidadResponse zombie) {

	public static ExplosionResponse desde(ResultadoExplosion resultado) {
		return new ExplosionResponse(
				resultado.danoExplosion(),
				resultado.jugadorAfectado(),
				resultado.zombieAfectado(),
				EntidadResponse.desde(resultado.creeper()),
				EntidadResponse.desde(resultado.jugador()),
				EntidadResponse.desde(resultado.zombie()));
	}
}
