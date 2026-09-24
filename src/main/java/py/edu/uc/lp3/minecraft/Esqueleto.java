package py.edu.uc.lp3.minecraft;

public class Esqueleto extends Mob {

	private int flechas;

	public Esqueleto(int vida, int danoBase, int velocidad, int flechas) {
		super(vida, danoBase, velocidad, true);
		setFlechas(flechas);
	}

	public int getFlechas() {
		return flechas;
	}

	public void setFlechas(int flechas) {
		if (flechas < 0) {
			throw new IllegalArgumentException("La cantidad de flechas no puede ser negativa");
		}
		this.flechas = flechas;
	}

	public void disparar(Jugador jugador) {
		if (jugador == null) {
			throw new IllegalArgumentException("El jugador es obligatorio");
		}
		if (flechas == 0) {
			throw new IllegalStateException("El esqueleto se quedo sin flechas");
		}
		flechas--;
		jugador.recibirDano(getDanoBase());
	}

	public void recargar(int cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La recarga debe ser un valor positivo");
		}
		setFlechas(flechas + cantidad);
	}

	@Override
	public String comportamiento() {
		return "El esqueleto es un mob hostil que dispara flechas a distancia sobre el jugador";
	}
}