package py.edu.uc.lp3.service.exception;

public class CompaneroNoDisponibleException extends IntegracionCompaneroException {

	public CompaneroNoDisponibleException(String companero, String detalle) {
		super(companero, "No se pudo contactar a " + companero + ": " + detalle);
	}
}
