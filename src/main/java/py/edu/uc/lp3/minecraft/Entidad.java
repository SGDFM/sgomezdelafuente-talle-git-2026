package py.edu.uc.lp3.minecraft;

public abstract class Entidad {

	private int vida;
	private int danoBase;
	private int velocidad;
	private int x;
	private int y;

	public Entidad(int vida, int danoBase, int velocidad) {
		setVida(vida);
		setDanoBase(danoBase);
		setVelocidad(velocidad);
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = Math.max(0, vida);
	}

	public int getDanoBase() {
		return danoBase;
	}

	public void setDanoBase(int danoBase) {
		if (danoBase < 0) {
			throw new IllegalArgumentException("El dano base no puede ser negativo");
		}
		this.danoBase = danoBase;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		if (velocidad < 0) {
			throw new IllegalArgumentException("La velocidad no puede ser negativa");
		}
		this.velocidad = velocidad;
	}

	public boolean estaViva() {
		return vida > 0;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void moverse(int deltaX, int deltaY) {
		if (!estaViva()) {
			return;
		}
		x += deltaX * velocidad;
		y += deltaY * velocidad;
	}

	public void recibirDano(int dano) {
		if (dano < 0) {
			throw new IllegalArgumentException("El dano recibido no puede ser negativo");
		}
		setVida(vida - dano);
	}

	public void desaparecer() {
		setVida(0);
	}

	public abstract String comportamiento();
}
