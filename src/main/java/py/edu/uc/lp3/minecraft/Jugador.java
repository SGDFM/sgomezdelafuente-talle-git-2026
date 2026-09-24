package py.edu.uc.lp3.minecraft;

public class Jugador extends Entidad {

	private static final int VIDA_MAXIMA = 20;

	private int armadura;

	public Jugador(int vida, int danoBase, int velocidad, int armadura) {
		super(vida, danoBase, velocidad);
		setArmadura(armadura);
	}

	public int getArmadura() {
		return armadura;
	}

	public void setArmadura(int armadura) {
		if (armadura < 0) {
			throw new IllegalArgumentException("La armadura no puede ser negativa");
		}
		this.armadura = armadura;
	}

	@Override
	public void setVida(int vida) {
		super.setVida(Math.min(vida, VIDA_MAXIMA));
	}

	@Override
	public void recibirDano(int dano) {
		if (dano < 0) {
			throw new IllegalArgumentException("El dano recibido no puede ser negativo");
		}
		int danoFinal = Math.max(0, dano - armadura);
		setVida(getVida() - danoFinal);
	}

	public void curar(int cantidad) {
		if (cantidad < 0) {
			throw new IllegalArgumentException("La curacion no puede ser negativa");
		}
		setVida(getVida() + cantidad);
	}

	@Override
	public String comportamiento() {
		return "El jugador explora, recibe dano reducido por armadura y puede curarse";
	}
}
