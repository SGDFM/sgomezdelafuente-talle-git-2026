package py.edu.uc.lp3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Jugador;
import py.edu.uc.lp3.domain.minecraft.Zombie;
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.service.EntidadService;

@SpringBootTest
@AutoConfigureMockMvc
class Poo08CreeperAdversarialTest {

	private static final String ENTIDADES = "/minecraft/sgomezdelafuente/entidades";
	private static final String SIMULACION = "/minecraft/sgomezdelafuente/simulaciones/explosion";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EntidadService service;

	@Autowired
	private EntidadRepository repository;

	@BeforeEach
	void limpiarBase() {
		repository.deleteAll();
	}

	@ParameterizedTest(name = "rechaza Creeper inválido: {0}")
	@MethodSource("creepersInvalidos")
	void rechazaEstadosInvalidosSinPersistir(String caso, String body) throws Exception {
		mockMvc.perform(post(ENTIDADES).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest());

		assertEquals(0, repository.count(), caso);
	}

	@Test
	void putConservaPosicionYNoPermiteCambiarSubtipo() throws Exception {
		Creeper creeper = new Creeper(20, 0, 2, 4);
		creeper.setX(8);
		creeper.setY(9);
		creeper = (Creeper) service.crear(creeper);

		mockMvc.perform(put(ENTIDADES + "/{id}", creeper.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"tipo":"CREEPER","vida":18,"danoBase":1,"velocidad":3,"cargaExplosion":5}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.x").value(8))
				.andExpect(jsonPath("$.y").value(9));

		mockMvc.perform(put(ENTIDADES + "/{id}", creeper.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"tipo":"ZOMBIE","vida":20,"danoBase":4,"velocidad":1}
						"""))
				.andExpect(status().isBadRequest());

		Creeper persistido = assertInstanceOf(Creeper.class, repository.findById(creeper.getId()).orElseThrow());
		assertEquals(18, persistido.getVida());
		assertEquals(8, persistido.getX());
		assertEquals(9, persistido.getY());
		assertEquals(5, persistido.getCargaExplosion());
	}

	@Test
	void simulacionesInvalidasNoCambianNingunaEntidad() throws Exception {
		Creeper creeper = (Creeper) service.crear(new Creeper(20, 0, 2, 4));
		Jugador jugador = (Jugador) service.crear(new Jugador(20, 4, 3, 2));
		Zombie zombie = (Zombie) service.crear(new Zombie(20, 5, 1));

		String[] pedidosInvalidos = {
				"null",
				"{\"creeperId\":null,\"jugadorId\":" + jugador.getId() + ",\"zombieId\":" + zombie.getId() + ",\"radio\":5}",
				"{\"creeperId\":" + jugador.getId() + ",\"jugadorId\":" + creeper.getId() + ",\"zombieId\":" + zombie.getId() + ",\"radio\":5}",
				"{\"creeperId\":" + creeper.getId() + ",\"jugadorId\":" + jugador.getId() + ",\"zombieId\":" + zombie.getId() + ",\"radio\":-1}"
		};
		for (String pedido : pedidosInvalidos) {
			mockMvc.perform(post(SIMULACION).contentType(MediaType.APPLICATION_JSON).content(pedido))
					.andExpect(status().isBadRequest());
		}

		mockMvc.perform(post(SIMULACION)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"creeperId":%d,"jugadorId":%d,"zombieId":999999,"radio":5}
						""".formatted(creeper.getId(), jugador.getId())))
				.andExpect(status().isNotFound());

		assertEquals(20, repository.findById(creeper.getId()).orElseThrow().getVida());
		assertEquals(4, ((Creeper) repository.findById(creeper.getId()).orElseThrow()).getCargaExplosion());
		assertEquals(20, repository.findById(jugador.getId()).orElseThrow().getVida());
		assertEquals(20, repository.findById(zombie.getId()).orElseThrow().getVida());
	}

	@Test
	void cargaInsuficienteYSegundaDetonacionNoAlteranEstado() throws Exception {
		Creeper sinCarga = (Creeper) service.crear(new Creeper(20, 0, 2, 2));
		Jugador jugador = (Jugador) service.crear(new Jugador(20, 4, 3, 2));
		Zombie zombie = (Zombie) service.crear(new Zombie(20, 5, 1));

		mockMvc.perform(post(SIMULACION)
				.contentType(MediaType.APPLICATION_JSON)
				.content(pedido(sinCarga, jugador, zombie, 5)))
				.andExpect(status().isConflict());
		assertEquals(20, repository.findById(sinCarga.getId()).orElseThrow().getVida());
		assertEquals(2, ((Creeper) repository.findById(sinCarga.getId()).orElseThrow()).getCargaExplosion());

		repository.deleteAll();
		Creeper explosivo = (Creeper) service.crear(new Creeper(20, 0, 2, 3));
		jugador = (Jugador) service.crear(new Jugador(20, 4, 3, 2));
		zombie = (Zombie) service.crear(new Zombie(20, 5, 1));
		String pedido = pedido(explosivo, jugador, zombie, 5);

		mockMvc.perform(post(SIMULACION).contentType(MediaType.APPLICATION_JSON).content(pedido))
				.andExpect(status().isOk());
		int vidaJugador = repository.findById(jugador.getId()).orElseThrow().getVida();
		int vidaZombie = repository.findById(zombie.getId()).orElseThrow().getVida();

		mockMvc.perform(post(SIMULACION).contentType(MediaType.APPLICATION_JSON).content(pedido))
				.andExpect(status().isConflict());
		assertEquals(0, repository.findById(explosivo.getId()).orElseThrow().getVida());
		assertEquals(vidaJugador, repository.findById(jugador.getId()).orElseThrow().getVida());
		assertEquals(vidaZombie, repository.findById(zombie.getId()).orElseThrow().getVida());
	}

	@Test
	void coordenadasExtremasNoAcercanVictimasPorOverflow() throws Exception {
		Creeper creeper = new Creeper(20, 0, 2, 3);
		creeper.setX(Integer.MIN_VALUE);
		creeper = (Creeper) service.crear(creeper);
		Jugador jugador = (Jugador) service.crear(new Jugador(20, 4, 3, 2));
		Zombie zombie = (Zombie) service.crear(new Zombie(20, 5, 1));

		mockMvc.perform(post(SIMULACION)
				.contentType(MediaType.APPLICATION_JSON)
				.content(pedido(creeper, jugador, zombie, 1)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jugadorAfectado").value(false))
				.andExpect(jsonPath("$.zombieAfectado").value(false));

		assertEquals(20, repository.findById(jugador.getId()).orElseThrow().getVida());
		assertEquals(20, repository.findById(zombie.getId()).orElseThrow().getVida());
	}

	private static Stream<Arguments> creepersInvalidos() {
		return Stream.of(
				Arguments.of("vida cero", "{\"tipo\":\"CREEPER\",\"vida\":0,\"danoBase\":0,\"velocidad\":2,\"cargaExplosion\":3}"),
				Arguments.of("vida negativa", "{\"tipo\":\"CREEPER\",\"vida\":-1,\"danoBase\":0,\"velocidad\":2,\"cargaExplosion\":3}"),
				Arguments.of("vida superior a 20", "{\"tipo\":\"CREEPER\",\"vida\":21,\"danoBase\":0,\"velocidad\":2,\"cargaExplosion\":3}"),
				Arguments.of("daño negativo", "{\"tipo\":\"CREEPER\",\"vida\":20,\"danoBase\":-1,\"velocidad\":2,\"cargaExplosion\":3}"),
				Arguments.of("velocidad negativa", "{\"tipo\":\"CREEPER\",\"vida\":20,\"danoBase\":0,\"velocidad\":-1,\"cargaExplosion\":3}"),
				Arguments.of("carga negativa", "{\"tipo\":\"CREEPER\",\"vida\":20,\"danoBase\":0,\"velocidad\":2,\"cargaExplosion\":-1}"),
				Arguments.of("carga con overflow", "{\"tipo\":\"CREEPER\",\"vida\":20,\"danoBase\":0,\"velocidad\":2,\"cargaExplosion\":536870912}"),
				Arguments.of("carga decimal", "{\"tipo\":\"CREEPER\",\"vida\":20,\"danoBase\":0,\"velocidad\":2,\"cargaExplosion\":-0.5}"),
				Arguments.of("id impuesto por cliente", "{\"id\":999,\"tipo\":\"CREEPER\",\"vida\":20,\"danoBase\":0,\"velocidad\":2,\"cargaExplosion\":3}"),
				Arguments.of("campos obligatorios omitidos", "{\"tipo\":\"CREEPER\",\"vida\":20}"),
				Arguments.of("tipo en minúsculas", "{\"tipo\":\"creeper\",\"vida\":20,\"danoBase\":0,\"velocidad\":2,\"cargaExplosion\":3}"));
	}

	private static String pedido(Creeper creeper, Jugador jugador, Zombie zombie, int radio) {
		return """
				{"creeperId":%d,"jugadorId":%d,"zombieId":%d,"radio":%d}
				""".formatted(creeper.getId(), jugador.getId(), zombie.getId(), radio);
	}
}
