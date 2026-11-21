package py.edu.uc.lp3.rest.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.rest.dto.EntidadRequest;
import py.edu.uc.lp3.rest.dto.EntidadResponse;
import py.edu.uc.lp3.rest.dto.UIResponse;
import py.edu.uc.lp3.service.EntidadService;
import py.edu.uc.lp3.service.JuegoService;

@RestController
@RequestMapping("/minecraft/sgomezdelafuente/entidades")
public class EntidadController {

	private final EntidadService service;
	private final JuegoService juegoService;

	public EntidadController(EntidadService service, JuegoService juegoService) {
		this.service = service;
		this.juegoService = juegoService;
	}

	@PostMapping
	public ResponseEntity<EntidadResponse> crear(@RequestBody EntidadRequest request) {
		Entidad creada = service.crear(request.aEntidad());
		return ResponseEntity.created(URI.create("/minecraft/sgomezdelafuente/entidades/" + creada.getId()))
				.body(respuesta(creada));
	}

	@GetMapping
	public List<EntidadResponse> listar() {
		return service.listar().stream().map(this::respuesta).toList();
	}

	@GetMapping("/{id}")
	public EntidadResponse buscar(@PathVariable Long id) {
		return respuesta(service.buscar(id));
	}

	@PutMapping("/{id}")
	public EntidadResponse actualizar(@PathVariable Long id, @RequestBody EntidadRequest request) {
		return respuesta(service.actualizar(id, request.aEntidad()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/ui")
	public List<UIResponse> describirUI() {
		return service.listarVivas().stream()
				.map(entidad -> UIResponse.desde(entidad, juegoService.describirUI(entidad)))
				.toList();
	}

	private EntidadResponse respuesta(Entidad entidad) {
		return EntidadResponse.desde(entidad, juegoService.comportamiento(entidad));
	}
}
