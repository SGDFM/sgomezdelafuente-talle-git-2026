package py.edu.uc.lp3.rest.dto;

import java.time.Instant;

public record IntegracionErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String companero,
		Integer estadoRemoto,
		String detalleRemoto,
		String mensaje,
		String path) {
}
