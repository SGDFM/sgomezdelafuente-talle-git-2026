package py.edu.uc.lp3.minecraft;

public class Creeper extends Mob {

	private int cargaExplosion;

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
		desaparecer();
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
}
