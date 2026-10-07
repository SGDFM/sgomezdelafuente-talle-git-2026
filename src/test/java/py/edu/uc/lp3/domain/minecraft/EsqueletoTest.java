package py.edu.uc.lp3.domain.minecraft;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EsqueletoTest {

	@Test
	void esqueletoEsHostilYSeTrataComoEntidad() {
		Entidad esqueleto = new Esqueleto(20, 5, 2, 3);

		assertTrue(((Esqueleto) esqueleto).isHostil());
		assertTrue(esqueleto.comportamiento().contains("esqueleto"));
	}

	@Test
	void esqueletoDisparaYConsumeFlechas() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 3);

		esqueleto.disparar(jugador);

		assertEquals(16, jugador.getVida());
		assertEquals(2, esqueleto.getFlechas());
		assertTrue(jugador.estaViva());
	}

	@Test
	void esqueletoTieneConstructoresSobrecargados() {
		Esqueleto porDefecto = new Esqueleto();
		Esqueleto conFlechas = new Esqueleto(8);

		assertEquals(5, porDefecto.getFlechas());
		assertEquals(8, conFlechas.getFlechas());
		assertTrue(porDefecto.estaViva());
	}

	@Test
	void dispararEstaSobrecargadoPorDistancia() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 2);

		esqueleto.disparar(jugador, 15);

		assertEquals(18, jugador.getVida());
		assertEquals(1, esqueleto.getFlechas());
		assertThrows(IllegalArgumentException.class, () -> esqueleto.disparar(jugador, 0));
	}

	@Test
	void esqueletoSinFlechasNoDispara() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 0);

		assertThrows(IllegalStateException.class, () -> esqueleto.disparar(jugador));
		assertEquals(20, jugador.getVida());
	}

	@Test
	void esqueletoRechazaFlechasNegativas() {
		assertThrows(IllegalArgumentException.class, () -> new Esqueleto(20, 5, 2, -1));

		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 1);
		assertThrows(IllegalArgumentException.class, () -> esqueleto.setFlechas(-1));
		assertEquals(1, esqueleto.getFlechas());
	}

	@Test
	void esqueletoRecargaFlechasSoloConPositivos() {
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 2);

		esqueleto.recargar(4);

		assertEquals(6, esqueleto.getFlechas());
		assertThrows(IllegalArgumentException.class, () -> esqueleto.recargar(0));
		assertEquals(6, esqueleto.getFlechas());
	}

	@Test
	void esqueletoPuedeMatarAlJugadorDisparando() {
		Jugador jugador = new Jugador(20, 4, 2, 1);
		Esqueleto esqueleto = new Esqueleto(20, 5, 2, 5);

		for (int i = 0; i < 5; i++) {
			esqueleto.disparar(jugador);
		}

		assertEquals(0, jugador.getVida());
		assertFalse(jugador.estaViva());
	}
}
