package py.edu.uc.lp3.rest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.domain.minecraft.Jugador;
import py.edu.uc.lp3.domain.minecraft.Zombie;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
		@JsonSubTypes.Type(value = EntidadRequest.JugadorRequest.class, name = "JUGADOR"),
		@JsonSubTypes.Type(value = EntidadRequest.CreeperRequest.class, name = "CREEPER"),
		@JsonSubTypes.Type(value = EntidadRequest.ZombieRequest.class, name = "ZOMBIE")
})
public sealed interface EntidadRequest {

	Entidad aEntidad();

	record JugadorRequest(int vida, int danoBase, int velocidad, Integer x, Integer y, int armadura) implements EntidadRequest {

		@Override
		public Entidad aEntidad() {
			Jugador jugador = new Jugador(vida, danoBase, velocidad, armadura);
			jugador.setX(coordenada(x));
			jugador.setY(coordenada(y));
			return jugador;
		}
	}

	record CreeperRequest(int vida, int danoBase, int velocidad, Integer x, Integer y, int cargaExplosion) implements EntidadRequest {

		@Override
		public Entidad aEntidad() {
			Creeper creeper = new Creeper(vida, danoBase, velocidad, cargaExplosion);
			creeper.setX(coordenada(x));
			creeper.setY(coordenada(y));
			return creeper;
		}
	}

	record ZombieRequest(int vida, int danoBase, int velocidad, Integer x, Integer y) implements EntidadRequest {

		@Override
		public Entidad aEntidad() {
			Zombie zombie = new Zombie(vida, danoBase, velocidad);
			zombie.setX(coordenada(x));
			zombie.setY(coordenada(y));
			return zombie;
		}
	}

	private static int coordenada(Integer valor) {
		return valor == null ? 0 : valor;
	}
}
