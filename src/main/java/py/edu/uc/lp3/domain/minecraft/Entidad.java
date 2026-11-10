package py.edu.uc.lp3.domain.minecraft;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "entidades")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_entidad")
public abstract class Entidad implements UICapable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int vida;

	@Column(nullable = false)
	private int danoBase;

	@Column(nullable = false)
	private int velocidad;

	@Column(nullable = false)
	private int x;

	@Column(nullable = false)
	private int y;

	protected Entidad() {
		// Requerido por JPA; los constructores de negocio establecen el estado valido.
	}

	public Entidad(int vida, int danoBase, int velocidad) {
		setVida(vida);
		setDanoBase(danoBase);
		setVelocidad(velocidad);
	}

	public Long getId() {
		return id;
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

	public boolean recibirExplosion(Entidad origen, int dano, int radio) {
		if (origen == null) {
			throw new IllegalArgumentException("El origen de la explosion es obligatorio");
		}
		if (dano <= 0 || radio < 0) {
			throw new IllegalArgumentException("El dano debe ser positivo y el radio no puede ser negativo");
		}
		int distancia = Math.abs(x - origen.x) + Math.abs(y - origen.y);
		if (!estaViva() || distancia > radio) {
			return false;
		}
		recibirDano(dano);
		return true;
	}

	public void desaparecer() {
		setVida(0);
	}

	public abstract String comportamiento();
}
