package py.edu.uc.lp3.domain.minecraft;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ZOMBIE")
public class Zombie extends Mob {

	protected Zombie() {
		super();
	}

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

	@Override
	public String describirUI() {
		return "imagen=zombie.png, posicion=(" + getX() + "," + getY()
				+ "), sonido=gemido, velocidad=" + getVelocidad();
	}
}
