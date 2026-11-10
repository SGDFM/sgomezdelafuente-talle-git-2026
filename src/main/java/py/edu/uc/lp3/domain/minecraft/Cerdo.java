package py.edu.uc.lp3.domain.minecraft;

public class Cerdo extends Mob {

	private boolean montable;

	public Cerdo(int vida, int danoBase, int velocidad, boolean montable) {
		super(vida, danoBase, velocidad, false);
		this.montable = montable;
	}

	public boolean isMontable() {
		return montable;
	}

	public void setMontable(boolean montable) {
		this.montable = montable;
	}

	@Override
	public String comportamiento() {
		return montable ? "El cerdo no es hostil y puede ser montado" : "El cerdo no es hostil y no puede ser montado";
	}

	@Override
	public String describirUI() {
		return "imagen=cerdo.png, posicion=(" + getX() + "," + getY()
				+ "), sonido=grunido, velocidad=" + getVelocidad();
	}
}
