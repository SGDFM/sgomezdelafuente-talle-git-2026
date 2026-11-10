package py.edu.uc.lp3.rest.dto;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String error, String mensaje, String path) {
}
