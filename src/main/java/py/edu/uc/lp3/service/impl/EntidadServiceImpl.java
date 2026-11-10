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
import py.edu.uc.lp3.service.EntidadService;

@Service
@Transactional
public class EntidadServiceImpl implements EntidadService {

	private final EntidadRepository repository;

	public EntidadServiceImpl(EntidadRepository repository) {
		this.repository = repository;
	}

	@Override
	public Entidad crear(Entidad entidad) {
		validarEstadoInicial(entidad);
		return repository.save(entidad);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Entidad> listar() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Entidad> listarVivas() {
		return repository.findByVidaGreaterThanOrderByIdAsc(0);
	}

	@Override
	@Transactional(readOnly = true)
	public Entidad buscar(Long id) {
		return repository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException(id));
	}

	@Override
	public Entidad actualizar(Long id, Entidad reemplazo) {
		validarEstadoInicial(reemplazo);
		Entidad existente = buscar(id);
		if (!existente.getClass().equals(reemplazo.getClass())) {
			throw new EstadoEntidadInvalidoException("PUT no puede cambiar el tipo de una entidad existente");
		}

		existente.setVida(reemplazo.getVida());
		existente.setDanoBase(reemplazo.getDanoBase());
		existente.setVelocidad(reemplazo.getVelocidad());
		if (existente instanceof Jugador jugador && reemplazo instanceof Jugador jugadorNuevo) {
			jugador.setArmadura(jugadorNuevo.getArmadura());
		} else if (existente instanceof Creeper creeper && reemplazo instanceof Creeper creeperNuevo) {
			creeper.setCargaExplosion(creeperNuevo.getCargaExplosion());
		}
		return repository.save(existente);
	}

	@Override
	public void eliminar(Long id) {
		Entidad entidad = buscar(id);
		repository.delete(entidad);
	}

	private void validarEstadoInicial(Entidad entidad) {
		if (!(entidad instanceof Jugador || entidad instanceof Creeper || entidad instanceof Zombie)) {
			throw new EstadoEntidadInvalidoException("Solo se persisten JUGADOR, CREEPER y ZOMBIE");
		}
		if (entidad.getVida() <= 0) {
			throw new EstadoEntidadInvalidoException("La vida inicial debe ser mayor que cero");
		}
		if (entidad.getDanoBase() < 0 || entidad.getVelocidad() < 0) {
			throw new EstadoEntidadInvalidoException("Dano base y velocidad no pueden ser negativos");
		}
	}
}
