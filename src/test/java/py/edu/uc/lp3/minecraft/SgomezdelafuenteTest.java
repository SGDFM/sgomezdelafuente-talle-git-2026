package py.edu.uc.lp3.minecraft;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class SgomezdelafuenteTest {

	@Test
	void entidadMantieneVidaValida() {
		Entidad entidad = new Jugador(10, 2, 1, 0);

		entidad.setVida(-10);

		assertEquals(0, entidad.getVida());
		assertFalse(entidad.estaViva());
	}

	@Test
	void entidadesSeTratanUniformemente() {
		List<Entidad> entidades = List.of(
				new Jugador(20, 4, 2, 1),
				new Zombie(20, 5, 1),
				new ZombiePequeno(20, 3, 2, 4),
				new Creeper(20, 0, 2, 1),
				new Cerdo(10, 0, 2, true),
				new Aldeano(20, 0, 1, true));

		for (Entidad entidad : entidades) {
			entidad.moverse(1, 1);
			entidad.recibirDano(3);
			entidad.desaparecer();

			assertEquals(0, entidad.getVida());
			assertFalse(entidad.estaViva());
		}
	}

	@Test
	void entidadMuertaNoSeMueve() {
		Entidad entidad = new Zombie(10, 2, 3);

		entidad.desaparecer();
		entidad.moverse(1, 1);

		assertEquals(0, entidad.getX());
		assertEquals(0, entidad.getY());
	}

	@Test
	void noAceptaAtributosNegativos() {
		Entidad entidad = new Cerdo(10, 2, 1, true);

		assertThrows(IllegalArgumentException.class, () -> entidad.setDanoBase(-1));
		assertThrows(IllegalArgumentException.class, () -> entidad.setVelocidad(-1));
	}

	@Test
	void jugadorLimitaVidaYReduceDanioConArmadura() {
		Jugador jugador = new Jugador(30, 4, 2, 3);

		jugador.recibirDano(8);
		jugador.curar(100);

		assertEquals(20, jugador.getVida());
		assertEquals(3, jugador.getArmadura());
	}

	@Test
	void zombieAtacaJugadorConDanoBase() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Zombie zombie = new Zombie(20, 5, 1);

		zombie.atacar(jugador);

		assertEquals(16, jugador.getVida());
	}

	@Test
	void zombiePequenoCalculaVelocidadTotal() {
		ZombiePequeno zombiePequeno = new ZombiePequeno(20, 3, 2, 4);

		assertEquals(6, zombiePequeno.getVelocidadTotal());
		assertThrows(IllegalArgumentException.class, () -> zombiePequeno.setVelocidadAumentada(-1));
	}

	@Test
	void creeperAumentaCargaYExplota() {
		Creeper creeper = new Creeper(20, 0, 2, 1);

		creeper.aumentarCarga(4);
		creeper.explotar();

		assertEquals(0, creeper.getVida());
		assertEquals(0, creeper.getCargaExplosion());
		assertFalse(creeper.estaViva());
	}

	@Test
	void mobsNoHostilesConservanEspecializacion() {
		Cerdo cerdo = new Cerdo(10, 0, 2, true);
		Aldeano aldeano = new Aldeano(20, 0, 1, true);

		assertFalse(cerdo.isHostil());
		assertTrue(cerdo.isMontable());
		assertFalse(aldeano.isHostil());
		assertTrue(aldeano.puedeComercializar());
	}

	@Test
	void comportamientoEsPolimorficoDesdeTipoPadre() {
		Entidad creeper = new Creeper(20, 0, 2, 3);
		Entidad aldeano = new Aldeano(20, 0, 1, true);

		assertTrue(creeper.comportamiento().contains("creeper"));
		assertTrue(aldeano.comportamiento().contains("aldeano"));
	}
}
