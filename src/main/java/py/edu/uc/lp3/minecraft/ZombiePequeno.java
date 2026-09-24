package py.edu.uc.lp3.minecraft;

public class ZombiePequeno extends Zombie {

	private int velocidadAumentada;

	public ZombiePequeno(int vida, int danoBase, int velocidad, int velocidadAumentada) {
		super(vida, danoBase, velocidad);
		setVelocidadAumentada(velocidadAumentada);
	}

	public int getVelocidadAumentada() {
		return velocidadAumentada;
	}

	public void setVelocidadAumentada(int velocidadAumentada) {
		if (velocidadAumentada < 0) {
			throw new IllegalArgumentException("La velocidad aumentada no puede ser negativa");
		}
		this.velocidadAumentada = velocidadAumentada;
	}

	public int getVelocidadTotal() {
		return getVelocidad() + velocidadAumentada;
	}

	@Override
	public String comportamiento() {
		return "El zombie pequeno ataca como zombie y se mueve con velocidad total " + getVelocidadTotal();
	}
}
