package py.edu.uc.lp3.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Entidades", description = "CRUD polimórfico persistido en H2")
public class EntidadController {

	private final EntidadService service;
	private final JuegoService juegoService;

	public EntidadController(EntidadService service, JuegoService juegoService) {
		this.service = service;
		this.juegoService = juegoService;
	}

	@PostMapping
	@Operation(summary = "Crea una entidad", description = "Acepta Jugador, Creeper o Zombie con vida entre 1 y 20.")
	public ResponseEntity<EntidadResponse> crear(@RequestBody EntidadRequest request) {
		Entidad creada = service.crear(request.aEntidad());
		return ResponseEntity.created(URI.create("/minecraft/sgomezdelafuente/entidades/" + creada.getId()))
				.body(respuesta(creada));
	}

	@GetMapping
	@Operation(summary = "Lista las entidades persistidas")
	public List<EntidadResponse> listar() {
		return service.listar().stream().map(this::respuesta).toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca una entidad por ID")
	public EntidadResponse buscar(@PathVariable Long id) {
		return respuesta(service.buscar(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Reemplaza una entidad", description = "No permite cambiar el subtipo ni modificar una entidad muerta.")
	public EntidadResponse actualizar(@PathVariable Long id, @RequestBody EntidadRequest request) {
		return respuesta(service.actualizar(id, request.aEntidad()));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina una entidad")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/ui")
	@Operation(summary = "Describe la UI de las entidades vivas", description = "Usa el contrato UICapable desde la capa de servicio.")
	public List<UIResponse> describirUI() {
		return service.listarVivas().stream()
				.map(entidad -> UIResponse.desde(entidad, juegoService.describirUI(entidad)))
				.toList();
	}

	private EntidadResponse respuesta(Entidad entidad) {
		return EntidadResponse.desde(entidad, juegoService.comportamiento(entidad));
	}
}
