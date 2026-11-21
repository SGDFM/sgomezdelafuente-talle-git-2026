package py.edu.uc.lp3.domain.minecraft;

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
		this.velocidadAumentada = velocidadAumentada;
	}
}
