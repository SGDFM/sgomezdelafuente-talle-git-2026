package py.edu.uc.lp3.domain.minecraft;

import jakarta.persistence.Entity;

@Entity
public abstract class Mob extends Entidad {

	private boolean hostil;

	protected Mob() {
		super();
	}

	public Mob(int vida, int danoBase, int velocidad, boolean hostil) {
		super(vida, danoBase, velocidad);
		this.hostil = hostil;
	}

	public boolean isHostil() {
		return hostil;
	}
}
