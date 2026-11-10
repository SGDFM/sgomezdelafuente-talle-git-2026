package py.edu.uc.lp3.domain.minecraft.exception;

public abstract class MinecraftException extends RuntimeException {

	protected MinecraftException(String message) {
		super(message);
	}
}
