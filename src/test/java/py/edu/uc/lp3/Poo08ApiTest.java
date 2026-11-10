package py.edu.uc.lp3;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class Poo08ApiTest {

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

	@Test
	void recorreCrudCompletoPersistido() throws Exception {
		mockMvc.perform(post(ENTIDADES)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"tipo":"CREEPER","vida":20,"danoBase":0,"velocidad":2,"cargaExplosion":3}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.tipo").value("CREEPER"));

		Long id = repository.findAll().getFirst().getId();
		mockMvc.perform(get(ENTIDADES))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
		mockMvc.perform(get(ENTIDADES + "/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cargaExplosion").value(3));

		mockMvc.perform(put(ENTIDADES + "/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"tipo":"CREEPER","vida":18,"danoBase":0,"velocidad":3,"cargaExplosion":5}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.vida").value(18))
				.andExpect(jsonPath("$.cargaExplosion").value(5));

		mockMvc.perform(delete(ENTIDADES + "/{id}", id)).andExpect(status().isNoContent());
		mockMvc.perform(get(ENTIDADES + "/{id}", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("EntidadNoEncontradaException"));
	}

	@Test
	void postIlegalNoGuardaLaEntidad() throws Exception {
		mockMvc.perform(post(ENTIDADES)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"tipo":"ZOMBIE","vida":-1,"danoBase":5,"velocidad":1}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("EstadoEntidadInvalidoException"));

		mockMvc.perform(get(ENTIDADES))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void endpointUiUsaMensajesDeLaInterfaz() throws Exception {
		service.crear(new Jugador(20, 4, 3, 2));
		service.crear(new Creeper(20, 0, 2, 3));

		mockMvc.perform(get(ENTIDADES + "/ui"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].mensaje", containsString("imagen=jugador.png")))
				.andExpect(jsonPath("$[1].mensaje", containsString("imagen=creeper.png")));
	}

	@Test
	void simulacionPersisteExplosionSobreTresClases() throws Exception {
		Creeper creeper = (Creeper) service.crear(new Creeper(20, 0, 2, 3));
		Jugador jugador = (Jugador) service.crear(new Jugador(20, 4, 3, 2));
		Zombie zombie = (Zombie) service.crear(new Zombie(20, 5, 1));

		mockMvc.perform(post(SIMULACION)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"creeperId":%d,"jugadorId":%d,"zombieId":%d,"radio":5}
						""".formatted(creeper.getId(), jugador.getId(), zombie.getId())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.danoExplosion").value(12))
				.andExpect(jsonPath("$.creeper.vida").value(0))
				.andExpect(jsonPath("$.jugador.vida").value(10))
				.andExpect(jsonPath("$.zombie.vida").value(8));

		mockMvc.perform(get(ENTIDADES + "/{id}", jugador.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.vida").value(10));
	}

	@Test
	void handlersDistinguenTiposDeExcepcion() throws Exception {
		mockMvc.perform(get(ENTIDADES + "/999999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("EntidadNoEncontradaException"));

		Creeper creeper = (Creeper) service.crear(new Creeper(20, 0, 2, 1));
		Jugador jugador = (Jugador) service.crear(new Jugador());
		Zombie zombie = (Zombie) service.crear(new Zombie(20, 5, 1));
		mockMvc.perform(post(SIMULACION)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"creeperId":%d,"jugadorId":%d,"zombieId":%d,"radio":5}
						""".formatted(creeper.getId(), jugador.getId(), zombie.getId())))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.error").value("ExplosionNoPermitidaException"));
	}
}
