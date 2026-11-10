package py.edu.uc.lp3.service;

import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Jugador;
import py.edu.uc.lp3.domain.minecraft.Zombie;

public interface ExplosionService {

	ResultadoExplosion simular(Long creeperId, Long jugadorId, Long zombieId, int radio);

	record ResultadoExplosion(
			int danoExplosion,
			boolean jugadorAfectado,
			boolean zombieAfectado,
			Creeper creeper,
			Jugador jugador,
			Zombie zombie) {
	}
}
