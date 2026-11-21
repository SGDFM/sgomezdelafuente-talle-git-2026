package py.edu.uc.lp3.domain.minecraft;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CREEPER")
public class Creeper extends Mob {

	private int cargaExplosion;

	public Creeper() {
		this(20, 0, 2, 0);
	}

	public Creeper(int cargaExplosion) {
		this(20, 0, 2, cargaExplosion);
	}

	public Creeper(int vida, int danoBase, int velocidad, int cargaExplosion) {
		super(vida, danoBase, velocidad, true);
		setCargaExplosion(cargaExplosion);
	}

	public int getCargaExplosion() {
		return cargaExplosion;
	}

	public void setCargaExplosion(int cargaExplosion) {
		this.cargaExplosion = cargaExplosion;
	}
}
