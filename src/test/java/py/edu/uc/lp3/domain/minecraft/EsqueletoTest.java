package py.edu.uc.lp3.domain.minecraft;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import py.edu.uc.lp3.service.JuegoService;
import py.edu.uc.lp3.service.exception.EstadoEntidadInvalidoException;
import py.edu.uc.lp3.service.impl.JuegoServiceImpl;

class EsqueletoTest {

	private final JuegoService juegoService = new JuegoServiceImpl();

	@Test
	void esqueletoEsHostilYSeTrataComoEntidad() {
		Entidad esqueleto = new Esqueleto(20, 5, 2, 3);

		assertTrue(((Esqueleto) esqueleto).isHostil());
		assertTrue(juegoService.comportamiento(esqueleto).contains("esqueleto"));
	}

	@Test
	void servicioDisparaYConsumeFlechas() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 3);

		juegoService.disparar(esqueleto, jugador);

		assertEquals(16, jugador.getVida());
		assertEquals(2, esqueleto.getFlechas());
		assertTrue(juegoService.estaViva(jugador));
	}

	@Test
	void esqueletoTieneConstructoresSobrecargados() {
		Esqueleto porDefecto = new Esqueleto();
		Esqueleto conFlechas = new Esqueleto(8);

		assertEquals(5, porDefecto.getFlechas());
		assertEquals(8, conFlechas.getFlechas());
		assertTrue(juegoService.estaViva(porDefecto));
	}

	@Test
	void servicioSobrecargaDisparoPorDistancia() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 2);

		juegoService.disparar(esqueleto, jugador, 15);

		assertEquals(18, jugador.getVida());
		assertEquals(1, esqueleto.getFlechas());
		assertThrows(EstadoEntidadInvalidoException.class, () -> juegoService.disparar(esqueleto, jugador, 0));
	}

	@Test
	void servicioRechazaDisparoSinFlechas() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 0);

		assertThrows(EstadoEntidadInvalidoException.class, () -> juegoService.disparar(esqueleto, jugador));
		assertEquals(20, jugador.getVida());
	}

	@Test
	void servicioRechazaFlechasNegativas() {
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, -1);

		assertThrows(EstadoEntidadInvalidoException.class, () -> juegoService.validarEstado(esqueleto));
	}

	@Test
	void servicioRecargaSoloConValoresPositivos() {
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 2);

		juegoService.recargar(esqueleto, 4);

		assertEquals(6, esqueleto.getFlechas());
		assertThrows(EstadoEntidadInvalidoException.class, () -> juegoService.recargar(esqueleto, 0));
		assertEquals(6, esqueleto.getFlechas());
	}

	@Test
	void servicioPuedeDejarAlJugadorSinVida() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 5);

		for (int i = 0; i < 5; i++) {
			juegoService.disparar(esqueleto, jugador);
		}

		assertEquals(0, jugador.getVida());
		assertFalse(juegoService.estaViva(jugador));
	}
}
