package py.edu.uc.lp3.service.exception;

public abstract class MinecraftException extends RuntimeException {

	protected MinecraftException(String message) {
		super(message);
	}
}
