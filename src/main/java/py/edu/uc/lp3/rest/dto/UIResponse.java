package py.edu.uc.lp3.rest.dto;

import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.domain.minecraft.UICapable;

public record UIResponse(Long id, String tipo, String mensaje) {

	public static UIResponse desde(Entidad entidad) {
		UICapable elemento = entidad;
		return new UIResponse(entidad.getId(), entidad.getClass().getSimpleName(), elemento.describirUI());
	}
}
