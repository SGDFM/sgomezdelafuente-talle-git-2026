package py.edu.uc.lp3.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.domain.minecraft.Jugador;
import py.edu.uc.lp3.domain.minecraft.Mob;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EntidadResponse(
		Long id,
		String tipo,
		int vida,
		int danoBase,
		int velocidad,
		int x,
		int y,
		Boolean hostil,
		Integer armadura,
		Integer cargaExplosion,
		String comportamiento) {

	public static EntidadResponse desde(Entidad entidad) {
		Boolean hostil = entidad instanceof Mob mob ? mob.isHostil() : null;
		Integer armadura = entidad instanceof Jugador jugador ? jugador.getArmadura() : null;
		Integer carga = entidad instanceof Creeper creeper ? creeper.getCargaExplosion() : null;
		return new EntidadResponse(
				entidad.getId(),
				entidad.getClass().getSimpleName().toUpperCase(),
				entidad.getVida(),
				entidad.getDanoBase(),
				entidad.getVelocidad(),
				entidad.getX(),
				entidad.getY(),
				hostil,
				armadura,
				carga,
				entidad.comportamiento());
	}
}
