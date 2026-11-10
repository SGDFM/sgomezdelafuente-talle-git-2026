package py.edu.uc.lp3.domain.minecraft;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import py.edu.uc.lp3.domain.minecraft.exception.ExplosionNoPermitidaException;

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
	void creeperYJugadorTienenConstructoresSobrecargados() {
		Creeper creeperPorDefecto = new Creeper();
		Creeper creeperConCarga = new Creeper(4);
		Jugador jugadorPorDefecto = new Jugador();

		assertEquals(0, creeperPorDefecto.getCargaExplosion());
		assertEquals(4, creeperConCarga.getCargaExplosion());
		assertEquals(20, jugadorPorDefecto.getVida());
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

	@Test
	void elementosDibujablesCompartenUICapable() {
		List<UICapable> elementos = List.of(
				new Jugador(),
				new Zombie(20, 5, 1),
				new ZombiePequeno(20, 3, 2, 4),
				new Creeper(),
				new Esqueleto(),
				new Cerdo(10, 0, 2, true),
				new Aldeano(20, 0, 1, true));

		assertEquals(7, elementos.size());
		assertTrue(elementos.stream().allMatch(elemento -> elemento.describirUI().contains("imagen=")));
	}

	@Test
	void explosionRespetaCargaRadioYArmadura() {
		Creeper creeper = new Creeper(20, 0, 2, 3);
		Jugador jugador = new Jugador(20, 4, 3, 2);
		Zombie zombieLejano = new Zombie(20, 5, 2);
		zombieLejano.moverse(10, 0);

		int dano = creeper.detonar();
		assertTrue(jugador.recibirExplosion(creeper, dano, 5));
		assertFalse(zombieLejano.recibirExplosion(creeper, dano, 5));

		assertEquals(10, jugador.getVida());
		assertEquals(20, zombieLejano.getVida());
		assertFalse(creeper.estaViva());
	}

	@Test
	void explotarNoEvitaLaReglaDeCargaMinima() {
		Creeper creeper = new Creeper(1);

		assertThrows(ExplosionNoPermitidaException.class, creeper::explotar);
		assertTrue(creeper.estaViva());
		assertEquals(1, creeper.getCargaExplosion());
	}

	@Test
	void hostilidadNoTieneMutadorPublico() {
		assertThrows(NoSuchMethodException.class, () -> Mob.class.getMethod("setHostil", boolean.class));
	}
}
