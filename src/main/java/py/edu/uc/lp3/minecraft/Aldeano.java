package py.edu.uc.lp3.minecraft;

public class Aldeano extends Mob {

	private boolean comercializacion;

	public Aldeano(int vida, int danoBase, int velocidad, boolean comercializacion) {
		super(vida, danoBase, velocidad, false);
		this.comercializacion = comercializacion;
	}

	public boolean puedeComercializar() {
		return comercializacion;
	}

	public void setComercializacion(boolean comercializacion) {
		this.comercializacion = comercializacion;
	}

	@Override
	public String comportamiento() {
		return comercializacion ? "El aldeano no es hostil y puede comerciar" : "El aldeano no es hostil y no comercia";
	}
}
