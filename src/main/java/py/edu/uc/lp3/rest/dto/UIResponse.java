package py.edu.uc.lp3.rest.dto;

import py.edu.uc.lp3.domain.minecraft.Entidad;

public record UIResponse(Long id, String tipo, String mensaje) {

	public static UIResponse desde(Entidad entidad, String mensaje) {
		return new UIResponse(entidad.getId(), entidad.getClass().getSimpleName(), mensaje);
	}
}
