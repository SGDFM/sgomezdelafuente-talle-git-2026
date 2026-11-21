package py.edu.uc.lp3.service.exception;

public class EntidadNoEncontradaException extends MinecraftException {

	public EntidadNoEncontradaException(Long id) {
		super("No existe una entidad con el ID " + id);
	}
}
