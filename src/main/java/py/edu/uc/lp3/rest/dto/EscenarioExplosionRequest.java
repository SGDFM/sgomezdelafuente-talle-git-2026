package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Explosión coordinada que debe ser aceptada por Ruth y Matías")
public record EscenarioExplosionRequest(
		@Schema(example = "Explosión coordinada en una mina") String descripcion,
		RuthRequest ruth,
		MatiasRequest matias) {

	public record RuthRequest(@Schema(example = "1") Long creeperId) {
	}

	public record MatiasRequest(
			@Schema(example = "1") Long creeperId,
			@Schema(example = "true") Boolean cargado,
			@Schema(example = "true") Boolean encendido,
			List<VictimaRequest> victimas) {
	}

	public record VictimaRequest(
			@Schema(example = "2") Long hostilId,
			@Schema(example = "2.0") Double distancia) {
	}
}
