package py.edu.uc.lp3.service;

import java.util.List;
import py.edu.uc.lp3.domain.minecraft.Entidad;

public interface EntidadService {

	Entidad crear(Entidad entidad);

	List<Entidad> listar();

	List<Entidad> listarVivas();

	Entidad buscar(Long id);

	Entidad actualizar(Long id, Entidad reemplazo);

	void eliminar(Long id);
}
