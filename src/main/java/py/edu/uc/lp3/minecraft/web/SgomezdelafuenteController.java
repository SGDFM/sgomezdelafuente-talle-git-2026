package py.edu.uc.lp3.minecraft.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.minecraft.Aldeano;
import py.edu.uc.lp3.minecraft.Creeper;
import py.edu.uc.lp3.minecraft.Entidad;
import py.edu.uc.lp3.minecraft.Jugador;
import py.edu.uc.lp3.minecraft.Zombie;

@RestController
@RequestMapping("/minecraft/sgomezdelafuente")
public class SgomezdelafuenteController {

	private final Creeper creeper = new Creeper(20, 0, 2, 0);
	private final Jugador jugador = new Jugador(20, 4, 3, 2);
	private final Zombie zombie = new Zombie(20, 5, 1);

	@GetMapping("/creeper")
	public CreeperEstado ver() {
		return CreeperEstado.desde(creeper);
	}

	@PostMapping("/creeper/carga")
	public CreeperEstado aumentarCarga(@RequestParam int cantidad) {
		creeper.aumentarCarga(cantidad);
		return CreeperEstado.desde(creeper);
	}

	@PostMapping("/creeper/mover")
	public CreeperEstado moverCreeper(@RequestParam int x, @RequestParam int y) {
		creeper.moverse(x, y);
		return CreeperEstado.desde(creeper);
	}

	@PostMapping("/creeper/dano")
	public CreeperEstado danarCreeper(@RequestParam int cantidad) {
		creeper.recibirDano(cantidad);
		return CreeperEstado.desde(creeper);
	}

	@PostMapping("/creeper/desaparecer")
	public CreeperEstado desaparecerCreeper() {
		creeper.desaparecer();
		return CreeperEstado.desde(creeper);
	}

	@PostMapping("/creeper/explotar")
	public CreeperEstado explotar() {
		creeper.explotar();
		return CreeperEstado.desde(creeper);
	}

	@GetMapping("/jugador")
	public JugadorEstado verJugador() {
		return JugadorEstado.desde(jugador);
	}

	@PostMapping("/zombie/atacar")
	public JugadorEstado atacarJugador() {
		zombie.atacar(jugador);
		return JugadorEstado.desde(jugador);
	}

	@PostMapping("/jugador/curar")
	public JugadorEstado curarJugador(@RequestParam int cantidad) {
		jugador.curar(cantidad);
		return JugadorEstado.desde(jugador);
	}

	@PostMapping("/jugador/mover")
	public JugadorEstado moverJugador(@RequestParam int x, @RequestParam int y) {
		jugador.moverse(x, y);
		return JugadorEstado.desde(jugador);
	}

	@PostMapping("/jugador/dano")
	public JugadorEstado danarJugador(@RequestParam int cantidad) {
		jugador.recibirDano(cantidad);
		return JugadorEstado.desde(jugador);
	}

	@PostMapping("/jugador/desaparecer")
	public JugadorEstado desaparecerJugador() {
		jugador.desaparecer();
		return JugadorEstado.desde(jugador);
	}

	@GetMapping("/comportamientos")
	public ComportamientosRespuesta comportamientos(
			@RequestParam(defaultValue = "20") int vida,
			@RequestParam(defaultValue = "2") int velocidad,
			@RequestParam(defaultValue = "1") int cargaExplosion,
			@RequestParam(defaultValue = "true") boolean comercializacion) {
		Entidad primera = new Creeper(vida, 0, velocidad, cargaExplosion);
		Entidad segunda = new Aldeano(vida, 0, velocidad, comercializacion);
		return new ComportamientosRespuesta(EntidadResumen.desde(primera), EntidadResumen.desde(segunda));
	}

	public record CreeperEstado(
			int vida,
			int danoBase,
			int velocidad,
			int x,
			int y,
			boolean hostil,
			int cargaExplosion,
			boolean vivo) {

		static CreeperEstado desde(Creeper creeper) {
			return new CreeperEstado(
					creeper.getVida(),
					creeper.getDanoBase(),
					creeper.getVelocidad(),
					creeper.getX(),
					creeper.getY(),
					creeper.isHostil(),
					creeper.getCargaExplosion(),
					creeper.estaViva());
		}
	}

	public record JugadorEstado(int vida, int danoBase, int velocidad, int x, int y, int armadura, boolean vivo) {

		static JugadorEstado desde(Jugador jugador) {
			return new JugadorEstado(
					jugador.getVida(),
					jugador.getDanoBase(),
					jugador.getVelocidad(),
					jugador.getX(),
					jugador.getY(),
					jugador.getArmadura(),
					jugador.estaViva());
		}
	}

	public record EntidadResumen(String tipo, int vida, int velocidad, boolean vivo, String comportamiento) {

		static EntidadResumen desde(Entidad entidad) {
			return new EntidadResumen(
					entidad.getClass().getSimpleName(),
					entidad.getVida(),
					entidad.getVelocidad(),
					entidad.estaViva(),
					entidad.comportamiento());
		}
	}

	public record ComportamientosRespuesta(EntidadResumen primera, EntidadResumen segunda) {
	}
}
