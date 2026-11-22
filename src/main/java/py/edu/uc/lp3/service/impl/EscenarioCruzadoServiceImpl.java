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
		LOGGER.debug("A3 aceptó el escenario; A1 persiste la jugada en H2");

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
		if (request.ruth() == null || request.ruth().creeperId() == null || request.ruth().creeperId() <= 0) {
			throw new EstadoEntidadInvalidoException("El creeperId de Ruth debe ser positivo");
		}
		if (request.matias() == null || request.matias().creeperId() == null || request.matias().creeperId() <= 0) {
			throw new EstadoEntidadInvalidoException("El creeperId de Matías debe ser positivo");
		}
		if (request.matias().victimas() == null || request.matias().victimas().isEmpty()) {
			throw new EstadoEntidadInvalidoException("Matías necesita al menos una víctima con distancia");
		}
		if (request.matias().cargado() == null || request.matias().encendido() == null) {
			throw new EstadoEntidadInvalidoException("Los estados cargado y encendido de Matías son obligatorios");
		}
		boolean victimaInvalida = request.matias().victimas().stream().anyMatch(victima -> victima == null
				|| victima.hostilId() == null
				|| victima.hostilId() <= 0
				|| victima.distancia() == null
				|| !Double.isFinite(victima.distancia())
				|| victima.distancia() < 0);
		if (victimaInvalida) {
			throw new EstadoEntidadInvalidoException("Cada víctima de Matías necesita un ID positivo y distancia válida");
		}
	}
}
