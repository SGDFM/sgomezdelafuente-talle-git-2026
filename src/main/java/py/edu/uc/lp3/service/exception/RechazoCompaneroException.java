package py.edu.uc.lp3.service.exception;

public class RechazoCompaneroException extends IntegracionCompaneroException {

	private final int estadoRemoto;
	private final String cuerpoRemoto;

	public RechazoCompaneroException(String companero, int estadoRemoto, String cuerpoRemoto) {
		super(companero, companero + " rechazó el escenario con estado " + estadoRemoto);
		this.estadoRemoto = estadoRemoto;
		this.cuerpoRemoto = cuerpoRemoto;
	}

	public int getEstadoRemoto() {
		return estadoRemoto;
	}

	public String getCuerpoRemoto() {
		return cuerpoRemoto;
	}
}
