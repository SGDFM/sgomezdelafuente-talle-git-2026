package py.edu.uc.lp3.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.domain.minecraft.Aldeano;
import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.domain.minecraft.Esqueleto;

@RestController
@RequestMapping("/minecraft/sgomezdelafuente")
public class SgomezdelafuenteController {

	@GetMapping("/comportamientos")
	public ComportamientosRespuesta comportamientos(
			@RequestParam(defaultValue = "20") int vida,
			@RequestParam(defaultValue = "2") int velocidad,
			@RequestParam(defaultValue = "1") int cargaExplosion,
			@RequestParam(defaultValue = "5") int flechas,
			@RequestParam(defaultValue = "true") boolean comercializacion) {
		Entidad primera = new Creeper(vida, 0, velocidad, cargaExplosion);
		Entidad segunda = new Aldeano(vida, 0, velocidad, comercializacion);
		Entidad tercera = new Esqueleto(vida, 5, velocidad, flechas);
		return new ComportamientosRespuesta(EntidadResumen.desde(primera), EntidadResumen.desde(segunda), EntidadResumen.desde(tercera));
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

	public record ComportamientosRespuesta(EntidadResumen primera, EntidadResumen segunda, EntidadResumen tercera) {
	}
}
