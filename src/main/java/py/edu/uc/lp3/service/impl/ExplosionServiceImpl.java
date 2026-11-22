package py.edu.uc.lp3.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.domain.minecraft.Jugador;
import py.edu.uc.lp3.domain.minecraft.Zombie;
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.service.ExplosionService;
import py.edu.uc.lp3.service.JuegoService;
import py.edu.uc.lp3.service.exception.EntidadNoEncontradaException;
import py.edu.uc.lp3.service.exception.EstadoEntidadInvalidoException;

@Service
@Transactional
public class ExplosionServiceImpl implements ExplosionService {

	private final EntidadRepository repository;
	private final JuegoService juegoService;

	public ExplosionServiceImpl(EntidadRepository repository, JuegoService juegoService) {
		this.repository = repository;
		this.juegoService = juegoService;
	}

	@Override
	public ResultadoExplosion simular(Long creeperId, Long jugadorId, Long zombieId, int radio) {
		if (radio < 0) {
			throw new EstadoEntidadInvalidoException("El radio no puede ser negativo");
		}
		Creeper creeper = buscarComo(creeperId, Creeper.class);
		Jugador jugador = buscarComo(jugadorId, Jugador.class);
		Zombie zombie = buscarComo(zombieId, Zombie.class);

		int dano = juegoService.detonar(creeper);
		boolean jugadorAfectado = juegoService.recibirExplosion(jugador, creeper, dano, radio);
		boolean zombieAfectado = juegoService.recibirExplosion(zombie, creeper, dano, radio);
		repository.saveAll(List.of(creeper, jugador, zombie));

		return new ResultadoExplosion(dano, jugadorAfectado, zombieAfectado, creeper, jugador, zombie);
	}

	private <T extends Entidad> T buscarComo(Long id, Class<T> tipoEsperado) {
		if (id == null || id <= 0) {
			throw new EstadoEntidadInvalidoException("Los IDs de la simulación deben ser positivos");
		}
		Entidad entidad = repository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException(id));
		if (!tipoEsperado.isInstance(entidad)) {
			throw new EstadoEntidadInvalidoException(
					"La entidad " + id + " debe ser de tipo " + tipoEsperado.getSimpleName());
		}
		return tipoEsperado.cast(entidad);
	}
}
