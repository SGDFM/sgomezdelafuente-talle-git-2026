package py.edu.uc.lp3.integration;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import py.edu.uc.lp3.rest.dto.EscenarioExplosionRequest.RuthRequest;
import py.edu.uc.lp3.service.exception.CompaneroNoDisponibleException;
import py.edu.uc.lp3.service.exception.RechazoCompaneroException;

@Component
public class WebClientRuthExplosionClient implements RuthExplosionClient {

	private static final Logger LOGGER = LogManager.getLogger(WebClientRuthExplosionClient.class);
	private static final String COMPANERO = "A2 - Ruth Santacruz";

	private final WebClient webClient;
	private final String path;

	public WebClientRuthExplosionClient(
			WebClient.Builder builder,
			@Value("${integracion.ruth.base-url:http://localhost:8081}") String baseUrl,
			@Value("${integracion.ruth.explosion-path:/api/minecraft/simulaciones/explosion}") String path) {
		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
				.responseTimeout(Duration.ofSeconds(5));
		this.webClient = builder.clientConnector(new ReactorClientHttpConnector(httpClient)).baseUrl(baseUrl).build();
		this.path = path;
	}

	@Override
	public String validar(RuthRequest request) {
		LOGGER.debug("Inicio de validación con {}: creeperId={}", COMPANERO, request.creeperId());
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
							if (clientResponse.statusCode().is5xxServerError()) {
								return Mono.error(new CompaneroNoDisponibleException(
										COMPANERO, "estado remoto " + status + ": " + body));
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
