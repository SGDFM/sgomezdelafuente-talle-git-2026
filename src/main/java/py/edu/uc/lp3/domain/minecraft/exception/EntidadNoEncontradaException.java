package py.edu.uc.lp3.domain.minecraft.exception;

public class EntidadNoEncontradaException extends MinecraftException {

	public EntidadNoEncontradaException(Long id) {
		super("No existe una entidad con id " + id);
	}
}
