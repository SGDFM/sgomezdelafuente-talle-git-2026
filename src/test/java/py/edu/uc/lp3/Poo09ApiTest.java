package py.edu.uc.lp3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import py.edu.uc.lp3.integration.MatiasExplosionClient;
import py.edu.uc.lp3.integration.RuthExplosionClient;
import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.rest.dto.EscenarioExplosionRequest.MatiasRequest;
import py.edu.uc.lp3.rest.dto.EscenarioExplosionRequest.RuthRequest;
import py.edu.uc.lp3.service.exception.CompaneroNoDisponibleException;
import py.edu.uc.lp3.service.exception.RechazoCompaneroException;

@SpringBootTest
@AutoConfigureMockMvc
class Poo09ApiTest {

	private static final String ESCENARIOS = "/minecraft/sgomezdelafuente/escenarios/explosiones";
	private static final String PEDIDO = """
			{
			  "descripcion": "Explosión coordinada en una mina",
			  "ruth": {"creeperId": 1},
			  "matias": {
			    "creeperId": 10,
			    "cargado": true,
			    "encendido": true,
			    "victimas": [{"hostilId": 11, "distancia": 2.0}]
			  }
			}
			""";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JugadaCruzadaRepository repository;

	@MockitoBean
	private RuthExplosionClient ruthClient;

	@MockitoBean
	private MatiasExplosionClient matiasClient;

	@BeforeEach
	void limpiar() {
		repository.deleteAll();
		reset(ruthClient, matiasClient);
	}

	@Test
	void persisteSoloCuandoAmbosCompanerosAceptan() throws Exception {
		when(matiasClient.validar(any(MatiasRequest.class))).thenReturn("{\"simulacionId\":7}");
		when(ruthClient.validar(any(RuthRequest.class))).thenReturn("{\"resultado\":\"aceptado\"}");

		mockMvc.perform(post(ESCENARIOS).contentType(MediaType.APPLICATION_JSON).content(PEDIDO))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.resultado").value("ACEPTADA_POR_A2_Y_A3"));

		assertEquals(1, repository.count());
		mockMvc.perform(get(ESCENARIOS))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].descripcion").value("Explosión coordinada en una mina"));
	}

	@Test
	void rechazoDeRuthIdentificaA2YNoPersiste() throws Exception {
		when(ruthClient.validar(any(RuthRequest.class)))
				.thenThrow(new RechazoCompaneroException("A2 - Ruth Santacruz", 422, "regla-de-juego"));

		mockMvc.perform(post(ESCENARIOS).contentType(MediaType.APPLICATION_JSON).content(PEDIDO))
				.andExpect(status().isUnprocessableContent())
				.andExpect(jsonPath("$.companero").value("A2 - Ruth Santacruz"))
				.andExpect(jsonPath("$.estadoRemoto").value(422));

		assertEquals(0, repository.count());
		verifyNoInteractions(matiasClient);
	}

	@Test
	void rechazoDeMatiasIdentificaA3YNoPersiste() throws Exception {
		when(ruthClient.validar(any(RuthRequest.class))).thenReturn("{\"resultado\":\"aceptado\"}");
		when(matiasClient.validar(any(MatiasRequest.class)))
				.thenThrow(new RechazoCompaneroException("A3 - Matías Duarte", 409, "creeper destruido"));

		mockMvc.perform(post(ESCENARIOS).contentType(MediaType.APPLICATION_JSON).content(PEDIDO))
				.andExpect(status().isUnprocessableContent())
				.andExpect(jsonPath("$.companero").value("A3 - Matías Duarte"))
				.andExpect(jsonPath("$.estadoRemoto").value(409));

		assertEquals(0, repository.count());
	}

	@Test
	void servicioDeRuthNoDisponibleResponde502YNoPersiste() throws Exception {
		when(ruthClient.validar(any(RuthRequest.class)))
				.thenThrow(new CompaneroNoDisponibleException("A2 - Ruth Santacruz", "Connection refused"));

		mockMvc.perform(post(ESCENARIOS).contentType(MediaType.APPLICATION_JSON).content(PEDIDO))
				.andExpect(status().isBadGateway())
				.andExpect(jsonPath("$.companero").value("A2 - Ruth Santacruz"));

		assertEquals(0, repository.count());
		verifyNoInteractions(matiasClient);
	}

	@Test
	void servicioDeMatiasNoDisponibleResponde502() throws Exception {
		when(ruthClient.validar(any(RuthRequest.class))).thenReturn("{\"resultado\":\"aceptado\"}");
		when(matiasClient.validar(any(MatiasRequest.class)))
				.thenThrow(new CompaneroNoDisponibleException("A3 - Matías Duarte", "Connection refused"));

		mockMvc.perform(post(ESCENARIOS).contentType(MediaType.APPLICATION_JSON).content(PEDIDO))
				.andExpect(status().isBadGateway())
				.andExpect(jsonPath("$.companero").value("A3 - Matías Duarte"));

		assertEquals(0, repository.count());
	}

	@Test
	void pedidoLocalInvalidoNoConsultaCompaneros() throws Exception {
		String invalido = PEDIDO.replace("\"creeperId\": 10", "\"creeperId\": 0");

		mockMvc.perform(post(ESCENARIOS).contentType(MediaType.APPLICATION_JSON).content(invalido))
				.andExpect(status().isBadRequest());

		assertEquals(0, repository.count());
		verifyNoInteractions(matiasClient, ruthClient);
	}

	@Test
	void publicaContratoOpenApiYSwaggerUi() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi").exists())
				.andExpect(jsonPath("$.paths['" + ESCENARIOS + "']").exists());

		mockMvc.perform(get("/swagger-ui.html"))
				.andExpect(status().is3xxRedirection());
	}
}
