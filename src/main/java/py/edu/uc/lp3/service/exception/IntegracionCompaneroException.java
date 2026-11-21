package py.edu.uc.lp3.service.exception;

public abstract class IntegracionCompaneroException extends MinecraftException {

	private final String companero;

	protected IntegracionCompaneroException(String companero, String message) {
		super(message);
		this.companero = companero;
	}

	public String getCompanero() {
		return companero;
	}
}
