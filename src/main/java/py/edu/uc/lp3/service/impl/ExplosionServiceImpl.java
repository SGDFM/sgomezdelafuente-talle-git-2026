package py.edu.uc.lp3.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.domain.minecraft.Jugador;
import py.edu.uc.lp3.domain.minecraft.Zombie;
import py.edu.uc.lp3.domain.minecraft.exception.EntidadNoEncontradaException;
import py.edu.uc.lp3.domain.minecraft.exception.EstadoEntidadInvalidoException;
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.service.ExplosionService;

@Service
@Transactional
public class ExplosionServiceImpl implements ExplosionService {

	private final EntidadRepository repository;

	public ExplosionServiceImpl(EntidadRepository repository) {
		this.repository = repository;
	}

	@Override
	public ResultadoExplosion simular(Long creeperId, Long jugadorId, Long zombieId, int radio) {
		Creeper creeper = buscarComo(creeperId, Creeper.class);
		Jugador jugador = buscarComo(jugadorId, Jugador.class);
		Zombie zombie = buscarComo(zombieId, Zombie.class);

		int dano = creeper.detonar();
		boolean jugadorAfectado = jugador.recibirExplosion(creeper, dano, radio);
		boolean zombieAfectado = zombie.recibirExplosion(creeper, dano, radio);
		repository.saveAll(List.of(creeper, jugador, zombie));

		return new ResultadoExplosion(dano, jugadorAfectado, zombieAfectado, creeper, jugador, zombie);
	}

	private <T extends Entidad> T buscarComo(Long id, Class<T> tipoEsperado) {
		Entidad entidad = repository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException(id));
		if (!tipoEsperado.isInstance(entidad)) {
			throw new EstadoEntidadInvalidoException(
					"La entidad " + id + " debe ser de tipo " + tipoEsperado.getSimpleName());
		}
		return tipoEsperado.cast(entidad);
	}
}
