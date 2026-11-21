package py.edu.uc.lp3.integration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import py.edu.uc.lp3.rest.dto.EscenarioExplosionRequest.MatiasRequest;
import py.edu.uc.lp3.service.exception.CompaneroNoDisponibleException;
import py.edu.uc.lp3.service.exception.RechazoCompaneroException;

@Component
public class WebClientMatiasExplosionClient implements MatiasExplosionClient {

	private static final Logger LOGGER = LogManager.getLogger(WebClientMatiasExplosionClient.class);
	private static final String COMPANERO = "A3 - Matías Duarte";

	private final WebClient webClient;
	private final String path;

	public WebClientMatiasExplosionClient(
			WebClient.Builder builder,
			@Value("${integracion.matias.base-url:http://localhost:8082}") String baseUrl,
			@Value("${integracion.matias.explosion-path:/api/simulaciones/explosion}") String path) {
		this.webClient = builder.baseUrl(baseUrl).build();
		this.path = path;
	}

	@Override
	public String validar(MatiasRequest request) {
		LOGGER.debug("Inicio de validación con {}: creeperId={}, víctimas={}",
				COMPANERO, request.creeperId(), request.victimas() == null ? 0 : request.victimas().size());
		try {
			String response = webClient.post()
					.uri(path)
					.bodyValue(request)
					.exchangeToMono(clientResponse -> {
						int status = clientResponse.statusCode().value();
						return clientResponse.bodyToMono(String.class).defaultIfEmpty("").flatMap(body -> {
							LOGGER.debug("Respuesta de {}: status={}, body={}", COMPANERO, status, body);
							if (clientResponse.statusCode().is2xxSuccessful()) {
								return Mono.just(body);
							}
							return Mono.error(new RechazoCompaneroException(COMPANERO, status, body));
						});
					})
					.block();
			LOGGER.debug("Validación aceptada por {}", COMPANERO);
			return response == null ? "" : response;
		} catch (WebClientRequestException exception) {
			LOGGER.debug("No se pudo contactar a {}", COMPANERO, exception);
			throw new CompaneroNoDisponibleException(COMPANERO, exception.getMessage());
		}
	}
}
