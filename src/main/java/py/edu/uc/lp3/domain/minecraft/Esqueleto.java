package py.edu.uc.lp3.domain.minecraft;

public class Esqueleto extends Mob {

	private int flechas;

	public Esqueleto() {
		this(20, 5, 2, 5);
	}

	public Esqueleto(int flechas) {
		this(20, 5, 2, flechas);
	}

	public Esqueleto(int vida, int danoBase, int velocidad, int flechas) {
		super(vida, danoBase, velocidad, true);
		setFlechas(flechas);
	}

	public int getFlechas() {
		return flechas;
	}

	public void setFlechas(int flechas) {
		this.flechas = flechas;
	}
}
