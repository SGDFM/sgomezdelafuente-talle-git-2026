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

}
