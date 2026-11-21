package py.edu.uc.lp3.service;

import py.edu.uc.lp3.domain.minecraft.Creeper;
import py.edu.uc.lp3.domain.minecraft.Entidad;
import py.edu.uc.lp3.domain.minecraft.Esqueleto;
import py.edu.uc.lp3.domain.minecraft.Jugador;
import py.edu.uc.lp3.domain.minecraft.Zombie;
import py.edu.uc.lp3.domain.minecraft.ZombiePequeno;

public interface JuegoService extends UICapable {

	void validarEstado(Entidad entidad);

	boolean estaViva(Entidad entidad);

	void mover(Entidad entidad, int deltaX, int deltaY);

	void recibirDano(Entidad entidad, int dano);

	void curar(Jugador jugador, int cantidad);

	void atacar(Zombie zombie, Jugador jugador);

	int velocidadTotal(ZombiePequeno zombie);

	void aumentarCarga(Creeper creeper, int cantidad);

	int detonar(Creeper creeper);

	boolean recibirExplosion(Entidad objetivo, Entidad origen, int dano, int radio);

	void disparar(Esqueleto esqueleto, Jugador jugador);

	void disparar(Esqueleto esqueleto, Jugador jugador, int distancia);

	void recargar(Esqueleto esqueleto, int cantidad);

	String comportamiento(Entidad entidad);
}
