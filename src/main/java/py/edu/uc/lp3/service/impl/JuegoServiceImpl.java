package py.edu.uc.lp3.service.impl;

import org.springframework.stereotype.Service;
import py.edu.uc.lp3.domain.minecraft.Aldeano;
import py.edu.uc.lp3.domain.minecraft.Cerdo;
import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.domain.minecraft.Esqueleto;
import py.edu.uc.lp3.domain.minecraft.Jugador;
import py.edu.uc.lp3.domain.minecraft.Zombie;
import py.edu.uc.lp3.domain.minecraft.ZombiePequeno;
import py.edu.uc.lp3.service.JuegoService;
import py.edu.uc.lp3.service.exception.EstadoEntidadInvalidoException;
import py.edu.uc.lp3.service.exception.ExplosionNoPermitidaException;

@Service
public class JuegoServiceImpl implements JuegoService {

	private static final int VIDA_MAXIMA_JUGADOR = 20;
	private static final int CARGA_MINIMA_EXPLOSION = 3;

	@Override
	public void validarEstado(Entidad entidad) {
		if (entidad == null) {
			throw new EstadoEntidadInvalidoException("La entidad es obligatoria");
		}
		if (entidad.getVida() < 0 || entidad.getDanoBase() < 0 || entidad.getVelocidad() < 0) {
			throw new EstadoEntidadInvalidoException("La vida, el daño base y la velocidad no pueden ser negativos");
		}
		if (entidad instanceof Jugador jugador && jugador.getArmadura() < 0) {
			throw new EstadoEntidadInvalidoException("La armadura no puede ser negativa");
		}
		if (entidad instanceof Creeper creeper && creeper.getCargaExplosion() < 0) {
			throw new EstadoEntidadInvalidoException("La carga de explosión no puede ser negativa");
		}
		if (entidad instanceof Esqueleto esqueleto && esqueleto.getFlechas() < 0) {
			throw new EstadoEntidadInvalidoException("La cantidad de flechas no puede ser negativa");
		}
		if (entidad instanceof ZombiePequeno zombie && zombie.getVelocidadAumentada() < 0) {
			throw new EstadoEntidadInvalidoException("La velocidad aumentada no puede ser negativa");
		}
	}

	@Override
	public boolean estaViva(Entidad entidad) {
		requerirEntidad(entidad);
		return entidad.getVida() > 0;
	}

	@Override
	public void mover(Entidad entidad, int deltaX, int deltaY) {
		validarEstado(entidad);
		if (!estaViva(entidad)) {
			return;
		}
		entidad.setX(entidad.getX() + deltaX * entidad.getVelocidad());
		entidad.setY(entidad.getY() + deltaY * entidad.getVelocidad());
	}

	@Override
	public void recibirDano(Entidad entidad, int dano) {
		requerirEntidad(entidad);
		if (dano < 0) {
			throw new EstadoEntidadInvalidoException("El daño recibido no puede ser negativo");
		}
		int danoFinal = entidad instanceof Jugador jugador ? Math.max(0, dano - jugador.getArmadura()) : dano;
		entidad.setVida(Math.max(0, entidad.getVida() - danoFinal));
	}

	@Override
	public void curar(Jugador jugador, int cantidad) {
		requerirEntidad(jugador);
		if (cantidad < 0) {
			throw new EstadoEntidadInvalidoException("La curación no puede ser negativa");
		}
		jugador.setVida(Math.min(VIDA_MAXIMA_JUGADOR, jugador.getVida() + cantidad));
	}

	@Override
	public void atacar(Zombie zombie, Jugador jugador) {
		requerirEntidad(zombie);
		requerirEntidad(jugador);
		recibirDano(jugador, zombie.getDanoBase());
	}

	@Override
	public int velocidadTotal(ZombiePequeno zombie) {
		validarEstado(zombie);
		return zombie.getVelocidad() + zombie.getVelocidadAumentada();
	}

	@Override
	public void aumentarCarga(Creeper creeper, int cantidad) {
		requerirEntidad(creeper);
		if (cantidad <= 0) {
			throw new EstadoEntidadInvalidoException("La carga solo puede aumentar con valores positivos");
		}
		creeper.setCargaExplosion(creeper.getCargaExplosion() + cantidad);
	}

	@Override
	public int detonar(Creeper creeper) {
		validarEstado(creeper);
		if (!estaViva(creeper)) {
			throw new ExplosionNoPermitidaException("Un Creeper sin vida no puede detonar");
		}
		if (creeper.getCargaExplosion() < CARGA_MINIMA_EXPLOSION) {
			throw new ExplosionNoPermitidaException(
					"El Creeper necesita una carga mínima de " + CARGA_MINIMA_EXPLOSION);
		}
		int danoExplosion = creeper.getCargaExplosion() * 4;
		creeper.setVida(0);
		creeper.setCargaExplosion(0);
		return danoExplosion;
	}

	@Override
	public boolean recibirExplosion(Entidad objetivo, Entidad origen, int dano, int radio) {
		requerirEntidad(objetivo);
		requerirEntidad(origen);
		if (dano <= 0 || radio < 0) {
			throw new EstadoEntidadInvalidoException("El daño debe ser positivo y el radio no puede ser negativo");
		}
		int distancia = Math.abs(objetivo.getX() - origen.getX()) + Math.abs(objetivo.getY() - origen.getY());
		if (!estaViva(objetivo) || distancia > radio) {
			return false;
		}
		recibirDano(objetivo, dano);
		return true;
	}

	@Override
	public void disparar(Esqueleto esqueleto, Jugador jugador) {
		dispararConDano(esqueleto, jugador, esqueleto.getDanoBase());
	}

	@Override
	public void disparar(Esqueleto esqueleto, Jugador jugador, int distancia) {
		if (distancia <= 0) {
			throw new EstadoEntidadInvalidoException("La distancia debe ser positiva");
		}
		int dano = distancia > 10 ? Math.max(1, esqueleto.getDanoBase() - 2) : esqueleto.getDanoBase();
		dispararConDano(esqueleto, jugador, dano);
	}

	@Override
	public void recargar(Esqueleto esqueleto, int cantidad) {
		requerirEntidad(esqueleto);
		if (cantidad <= 0) {
			throw new EstadoEntidadInvalidoException("La recarga debe ser un valor positivo");
		}
		esqueleto.setFlechas(esqueleto.getFlechas() + cantidad);
	}

	@Override
	public String comportamiento(Entidad entidad) {
		requerirEntidad(entidad);
		if (entidad instanceof Jugador) {
			return "El jugador explora, recibe daño reducido por armadura y puede curarse";
		}
		if (entidad instanceof Creeper) {
			return "El Creeper aumenta su carga y explota; al desaparecer reinicia la carga";
		}
		if (entidad instanceof ZombiePequeno zombie) {
			return "El zombi pequeño ataca como zombi y se mueve con velocidad total " + velocidadTotal(zombie);
		}
		if (entidad instanceof Zombie) {
			return "El zombi persigue al jugador y ataca con su daño base";
		}
		if (entidad instanceof Esqueleto) {
			return "El esqueleto es un mob hostil que dispara flechas al jugador desde la distancia";
		}
		if (entidad instanceof Cerdo cerdo) {
			return cerdo.isMontable()
					? "El cerdo no es hostil y puede ser montado"
					: "El cerdo no es hostil y no puede ser montado";
		}
		if (entidad instanceof Aldeano aldeano) {
			return aldeano.puedeComercializar()
					? "El aldeano no es hostil y puede comerciar"
					: "El aldeano no es hostil y no comercia";
		}
		throw new EstadoEntidadInvalidoException("No existe una descripción para " + entidad.getClass().getSimpleName());
	}

	@Override
	public String describirUI(Entidad entidad) {
		requerirEntidad(entidad);
		String posicion = "posición=(" + entidad.getX() + "," + entidad.getY() + ")";
		if (entidad instanceof Jugador) {
			return "imagen=jugador.png, " + posicion + ", sonido=pasos, velocidad=" + entidad.getVelocidad();
		}
		if (entidad instanceof Creeper creeper) {
			return "imagen=creeper.png, " + posicion + ", sonido=siseo, velocidad=" + entidad.getVelocidad()
					+ ", cargaExplosion=" + creeper.getCargaExplosion();
		}
		if (entidad instanceof ZombiePequeno zombie) {
			return "imagen=zombie-pequeno.png, " + posicion + ", sonido=gemido-agudo, velocidad="
					+ velocidadTotal(zombie);
		}
		if (entidad instanceof Zombie) {
			return "imagen=zombie.png, " + posicion + ", sonido=gemido, velocidad=" + entidad.getVelocidad();
		}
		if (entidad instanceof Esqueleto) {
			return "imagen=esqueleto.png, " + posicion + ", sonido=huesos, velocidad=" + entidad.getVelocidad();
		}
		if (entidad instanceof Cerdo) {
			return "imagen=cerdo.png, " + posicion + ", sonido=gruñido, velocidad=" + entidad.getVelocidad();
		}
		if (entidad instanceof Aldeano) {
			return "imagen=aldeano.png, " + posicion + ", sonido=murmullo, velocidad=" + entidad.getVelocidad();
		}
		throw new EstadoEntidadInvalidoException("No existe una descripción de UI para " + entidad.getClass().getSimpleName());
	}

	private void dispararConDano(Esqueleto esqueleto, Jugador jugador, int dano) {
		validarEstado(esqueleto);
		requerirEntidad(jugador);
		if (esqueleto.getFlechas() == 0) {
			throw new EstadoEntidadInvalidoException("El esqueleto se quedó sin flechas");
		}
		esqueleto.setFlechas(esqueleto.getFlechas() - 1);
		recibirDano(jugador, dano);
	}

	private void requerirEntidad(Entidad entidad) {
		if (entidad == null) {
			throw new EstadoEntidadInvalidoException("La entidad es obligatoria");
		}
	}
}
