package py.edu.uc.lp3.rest.dto;

import java.time.Instant;
import py.edu.uc.lp3.domain.integration.JugadaCruzada;

public record JugadaCruzadaResponse(
		Long id,
		Instant creadaEn,
		String descripcion,
		Long creeperRuthId,
		Long creeperMatiasId,
		String resultado) {

	public static JugadaCruzadaResponse desde(JugadaCruzada jugada) {
		return new JugadaCruzadaResponse(
				jugada.getId(),
				jugada.getCreadaEn(),
				jugada.getDescripcion(),
				jugada.getCreeperRuthId(),
				jugada.getCreeperMatiasId(),
				"ACEPTADA_POR_A2_Y_A3");
	}
}
