package py.edu.uc.lp3.domain.minecraft;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import py.edu.uc.lp3.domain.minecraft.exception.ExplosionNoPermitidaException;

@Entity
@DiscriminatorValue("CREEPER")
public class Creeper extends Mob {

	public static final int CARGA_MINIMA_EXPLOSION = 3;

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
		if (cargaExplosion < 0) {
			throw new IllegalArgumentException("La carga de explosion no puede ser negativa");
		}
		this.cargaExplosion = cargaExplosion;
	}

	public void aumentarCarga(int cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La carga solo puede aumentar con valores positivos");
		}
		cargaExplosion += cantidad;
	}

	public void explotar() {
		detonar();
	}

	public int detonar() {
		if (!estaViva()) {
			throw new ExplosionNoPermitidaException("Un creeper sin vida no puede detonar");
		}
		if (cargaExplosion < CARGA_MINIMA_EXPLOSION) {
			throw new ExplosionNoPermitidaException(
					"El creeper necesita una carga minima de " + CARGA_MINIMA_EXPLOSION);
		}
		int danoExplosion = cargaExplosion * 4;
		desaparecer();
		return danoExplosion;
	}

	@Override
	public void desaparecer() {
		super.desaparecer();
		cargaExplosion = 0;
	}

	@Override
	public String comportamiento() {
		return "El creeper aumenta su carga y explota; al desaparecer reinicia la carga";
	}

	@Override
	public String describirUI() {
		return "imagen=creeper.png, posicion=(" + getX() + "," + getY()
				+ "), sonido=siseo, velocidad=" + getVelocidad()
				+ ", cargaExplosion=" + cargaExplosion;
	}
}
