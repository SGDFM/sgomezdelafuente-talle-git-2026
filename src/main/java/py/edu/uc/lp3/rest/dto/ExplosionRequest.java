package py.edu.uc.lp3.rest.dto;

public record ExplosionRequest(Long creeperId, Long jugadorId, Long zombieId, int radio) {
}
