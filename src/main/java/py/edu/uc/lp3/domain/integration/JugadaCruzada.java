package py.edu.uc.lp3.domain.integration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "jugadas_cruzadas")
public class JugadaCruzada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Instant creadaEn;

	@Column(nullable = false)
	private String descripcion;

	@Column(nullable = false)
	private Long creeperRuthId;

	@Column(nullable = false)
	private Long creeperMatiasId;

	@Lob
	@Column(nullable = false)
	private String respuestaRuth;

	@Lob
	@Column(nullable = false)
	private String respuestaMatias;

	protected JugadaCruzada() {
	}

	public JugadaCruzada(
			Instant creadaEn,
			String descripcion,
			Long creeperRuthId,
			Long creeperMatiasId,
			String respuestaRuth,
			String respuestaMatias) {
		this.creadaEn = creadaEn;
		this.descripcion = descripcion;
		this.creeperRuthId = creeperRuthId;
		this.creeperMatiasId = creeperMatiasId;
		this.respuestaRuth = respuestaRuth;
		this.respuestaMatias = respuestaMatias;
	}

	public Long getId() {
		return id;
	}

	public Instant getCreadaEn() {
		return creadaEn;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Long getCreeperRuthId() {
		return creeperRuthId;
	}

	public Long getCreeperMatiasId() {
		return creeperMatiasId;
	}

	public String getRespuestaRuth() {
		return respuestaRuth;
	}

	public String getRespuestaMatias() {
		return respuestaMatias;
	}
}
