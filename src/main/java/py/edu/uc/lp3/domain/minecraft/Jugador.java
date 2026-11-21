package py.edu.uc.lp3.domain.minecraft;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("JUGADOR")
public class Jugador extends Entidad {

	private int armadura;

	public Jugador() {
		this(20, 4, 3, 0);
	}

	public Jugador(int vida, int danoBase, int velocidad, int armadura) {
		super(vida, danoBase, velocidad);
		setArmadura(armadura);
	}

	public int getArmadura() {
		return armadura;
	}

	public void setArmadura(int armadura) {
		this.armadura = armadura;
	}
}
