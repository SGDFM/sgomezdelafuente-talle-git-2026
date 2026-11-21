package py.edu.uc.lp3.domain.minecraft;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import py.edu.uc.lp3.service.JuegoService;
import py.edu.uc.lp3.service.exception.EstadoEntidadInvalidoException;
import py.edu.uc.lp3.service.exception.ExplosionNoPermitidaException;
import py.edu.uc.lp3.service.impl.JuegoServiceImpl;

class SgomezdelafuenteTest {

	private final JuegoService juegoService = new JuegoServiceImpl();

	@Test
	void entidadSoloAlmacenaEstadoYElServicioLoValida() {
		Entidad entidad = new Jugador(10, 2, 1, 0);

		entidad.setVida(-10);

		assertEquals(-10, entidad.getVida());
		assertThrows(EstadoEntidadInvalidoException.class, () -> juegoService.validarEstado(entidad));
	}

	@Test
	void servicioTrataLasEntidadesUniformemente() {
		List<Entidad> entidades = List.of(
				new Jugador(20, 4, 2, 1),
				new Zombie(20, 5, 1),
				new ZombiePequeno(20, 3, 2, 4),
				new Creeper(20, 0, 2, 1),
				new Cerdo(10, 0, 2, true),
				new Aldeano(20, 0, 1, true));

		for (Entidad entidad : entidades) {
			juegoService.mover(entidad, 1, 1);
			juegoService.recibirDano(entidad, 3);
			entidad.setVida(0);

			assertEquals(0, entidad.getVida());
			assertFalse(juegoService.estaViva(entidad));
		}
	}

	@Test
	void servicioNoMueveUnaEntidadSinVida() {
		Entidad entidad = new Zombie(0, 2, 3);

		juegoService.mover(entidad, 1, 1);

		assertEquals(0, entidad.getX());
		assertEquals(0, entidad.getY());
	}

	@Test
	void servicioRechazaAtributosNegativos() {
		Entidad entidad = new Cerdo(10, -1, -1, true);

		assertThrows(EstadoEntidadInvalidoException.class, () -> juegoService.validarEstado(entidad));
	}

	@Test
	void servicioReduceDanoConArmaduraYLimitaCuracion() {
		Jugador jugador = new Jugador(20, 4, 2, 3);

		juegoService.recibirDano(jugador, 8);
		juegoService.curar(jugador, 100);

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
	void servicioAplicaAtaqueDeZombie() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Zombie zombie = new Zombie(20, 5, 1);

		juegoService.atacar(zombie, jugador);

		assertEquals(16, jugador.getVida());
	}

	@Test
	void servicioCalculaVelocidadTotal() {
		ZombiePequeno zombiePequeno = new ZombiePequeno(20, 3, 2, 4);

		assertEquals(6, juegoService.velocidadTotal(zombiePequeno));
		zombiePequeno.setVelocidadAumentada(-1);
		assertThrows(EstadoEntidadInvalidoException.class, () -> juegoService.velocidadTotal(zombiePequeno));
	}

	@Test
	void servicioAumentaCargaYDetonaCreeper() {
		Creeper creeper = new Creeper(20, 0, 2, 1);

		juegoService.aumentarCarga(creeper, 4);
		juegoService.detonar(creeper);

		assertEquals(0, creeper.getVida());
		assertEquals(0, creeper.getCargaExplosion());
		assertFalse(juegoService.estaViva(creeper));
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
	void servicioDescribeEntidadesDesdeElTipoPadre() {
		Entidad creeper = new Creeper(20, 0, 2, 3);
		Entidad aldeano = new Aldeano(20, 0, 1, true);

		assertTrue(juegoService.comportamiento(creeper).contains("Creeper"));
		assertTrue(juegoService.comportamiento(aldeano).contains("aldeano"));
	}

	@Test
	void servicioImplementaElProtocoloUICapable() {
		List<Entidad> elementos = List.of(
				new Jugador(),
				new Zombie(20, 5, 1),
				new ZombiePequeno(20, 3, 2, 4),
				new Creeper(),
				new Esqueleto(),
				new Cerdo(10, 0, 2, true),
				new Aldeano(20, 0, 1, true));

		assertEquals(7, elementos.size());
		assertTrue(elementos.stream().allMatch(elemento -> juegoService.describirUI(elemento).contains("imagen=")));
	}

	@Test
	void servicioRespetaCargaRadioYArmadura() {
		Creeper creeper = new Creeper(20, 0, 2, 3);
		Jugador jugador = new Jugador(20, 4, 3, 2);
		Zombie zombieLejano = new Zombie(20, 5, 2);
		juegoService.mover(zombieLejano, 10, 0);

		int dano = juegoService.detonar(creeper);
		assertTrue(juegoService.recibirExplosion(jugador, creeper, dano, 5));
		assertFalse(juegoService.recibirExplosion(zombieLejano, creeper, dano, 5));

		assertEquals(10, jugador.getVida());
		assertEquals(20, zombieLejano.getVida());
		assertFalse(juegoService.estaViva(creeper));
	}

	@Test
	void servicioAplicaLaCargaMinima() {
		Creeper creeper = new Creeper(1);

		assertThrows(ExplosionNoPermitidaException.class, () -> juegoService.detonar(creeper));
		assertTrue(juegoService.estaViva(creeper));
		assertEquals(1, creeper.getCargaExplosion());
	}

	@Test
	void hostilidadNoTieneMutadorPublico() {
		assertThrows(NoSuchMethodException.class, () -> Mob.class.getMethod("setHostil", boolean.class));
	}
}
