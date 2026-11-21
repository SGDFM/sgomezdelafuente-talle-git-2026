package py.edu.uc.lp3.service;

import java.util.List;
import py.edu.uc.lp3.domain.integration.JugadaCruzada;
import py.edu.uc.lp3.rest.dto.EscenarioExplosionRequest;

public interface EscenarioCruzadoService {

	JugadaCruzada ejecutar(EscenarioExplosionRequest request);

	List<JugadaCruzada> listar();
}
