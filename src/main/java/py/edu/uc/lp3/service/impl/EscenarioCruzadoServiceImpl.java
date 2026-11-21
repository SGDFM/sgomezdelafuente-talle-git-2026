package py.edu.uc.lp3.service.impl;

import java.time.Instant;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.uc.lp3.domain.integration.JugadaCruzada;
import py.edu.uc.lp3.integration.MatiasExplosionClient;
import py.edu.uc.lp3.integration.RuthExplosionClient;
import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.rest.dto.EscenarioExplosionRequest;
import py.edu.uc.lp3.service.EscenarioCruzadoService;
import py.edu.uc.lp3.service.exception.EstadoEntidadInvalidoException;

@Service
@Transactional
public class EscenarioCruzadoServiceImpl implements EscenarioCruzadoService {

	private static final Logger LOGGER = LogManager.getLogger(EscenarioCruzadoServiceImpl.class);

	private final RuthExplosionClient ruthClient;
	private final MatiasExplosionClient matiasClient;
	private final JugadaCruzadaRepository repository;

	public EscenarioCruzadoServiceImpl(
			RuthExplosionClient ruthClient,
			MatiasExplosionClient matiasClient,
			JugadaCruzadaRepository repository) {
		this.ruthClient = ruthClient;
		this.matiasClient = matiasClient;
		this.repository = repository;
	}

	@Override
	public JugadaCruzada ejecutar(EscenarioExplosionRequest request) {
		validar(request);
		LOGGER.debug("Inicio del escenario cruzado: {}", request.descripcion());

		String respuestaRuth = ruthClient.validar(request.ruth());
		LOGGER.debug("A2 aceptó el escenario; se consulta A3");
		String respuestaMatias = matiasClient.validar(request.matias());
		LOGGER.debug("A3 aceptó el escenario; se persiste la jugada en H2");

		JugadaCruzada jugada = new JugadaCruzada(
				Instant.now(),
				request.descripcion().trim(),
				request.ruth().creeperId(),
				request.matias().creeperId(),
				respuestaRuth,
				respuestaMatias);
		JugadaCruzada guardada = repository.save(jugada);
		LOGGER.debug("Escenario cruzado persistido con ID={}", guardada.getId());
		return guardada;
	}

	@Override
	@Transactional(readOnly = true)
	public List<JugadaCruzada> listar() {
		return repository.findAll();
	}

	private void validar(EscenarioExplosionRequest request) {
		if (request == null || request.descripcion() == null || request.descripcion().isBlank()) {
			throw new EstadoEntidadInvalidoException("La descripción del escenario es obligatoria");
		}
		if (request.ruth() == null || request.ruth().creeperId() == null) {
			throw new EstadoEntidadInvalidoException("El creeperId de Ruth es obligatorio");
		}
		if (request.matias() == null || request.matias().creeperId() == null) {
			throw new EstadoEntidadInvalidoException("El creeperId de Matías es obligatorio");
		}
		if (request.matias().victimas() == null || request.matias().victimas().isEmpty()) {
			throw new EstadoEntidadInvalidoException("Matías necesita al menos una víctima con distancia");
		}
	}
}
