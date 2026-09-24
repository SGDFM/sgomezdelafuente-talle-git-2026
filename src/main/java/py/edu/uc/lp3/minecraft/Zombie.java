package py.edu.uc.lp3.minecraft;

public class Zombie extends Mob {

	public Zombie(int vida, int danoBase, int velocidad) {
		super(vida, danoBase, velocidad, true);
	}

	public void atacar(Jugador jugador) {
		if (jugador == null) {
			throw new IllegalArgumentException("El jugador es obligatorio");
		}
		jugador.recibirDano(getDanoBase());
	}

	@Override
	public String comportamiento() {
		return "El zombie persigue al jugador y ataca con su dano base";
	}
}
