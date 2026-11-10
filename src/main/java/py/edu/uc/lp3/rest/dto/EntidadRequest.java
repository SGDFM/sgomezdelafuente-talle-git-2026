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

	record JugadorRequest(int vida, int danoBase, int velocidad, int armadura) implements EntidadRequest {

		@Override
		public Entidad aEntidad() {
			return new Jugador(vida, danoBase, velocidad, armadura);
		}
	}

	record CreeperRequest(int vida, int danoBase, int velocidad, int cargaExplosion) implements EntidadRequest {

		@Override
		public Entidad aEntidad() {
			return new Creeper(vida, danoBase, velocidad, cargaExplosion);
		}
	}

	record ZombieRequest(int vida, int danoBase, int velocidad) implements EntidadRequest {

		@Override
		public Entidad aEntidad() {
			return new Zombie(vida, danoBase, velocidad);
		}
	}
}
