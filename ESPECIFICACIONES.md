# Especificaciones POO-09

Este archivo resume las especificaciones de integración aplicadas al dominio Minecraft. La entrega completa para Classroom se encuentra en `CLASSROOM_ENTREGA.md`.

Commit de solución POO-08: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026/commit/58861f5135afe589ed34f2985874d3ef0c0087ef

Commit de solución POO-09 corregida: pendiente de commit local; no publicado.

## Alcance

- Contrato OpenAPI y Swagger UI de todos los controladores propios.
- Consumo con `WebClient` de la explosión de Ruth Santacruz (A2).
- Consumo con `WebClient` de la explosión de Matías Duarte (A3).
- URLs y rutas externas configurables mediante variables de entorno.
- Persistencia H2 de `JugadaCruzada` solo cuando ambos compañeros aceptan.
- Respuestas `422` para rechazos remotos y `502` para indisponibilidad.
- Trazas Log4j2 en nivel `DEBUG` para clientes y orquestador.
- Colección Insomnia POO-09 versionada.

## Aceptación

- `./mvnw test` termina correctamente.
- `./mvnw spring-boot:run` levanta la API.
- `/v3/api-docs` publica el contrato y `/swagger-ui.html` abre su interfaz.
- El camino feliz responde `201` y un GET posterior recupera la jugada desde H2.
- Un rechazo de A2 no consulta A3 ni modifica la H2 de A1.
- Un rechazo de A3 no modifica la H2 de A1.
- Un servicio no disponible responde `502` e identifica al compañero.
- `insomnia/poo-09-integracion.json` contiene el camino feliz y los rechazos de A2 y A3.
- Propiedades desconocidas y decimales en campos enteros responden `400`.
- La carga no puede desbordar el cálculo del daño y la distancia soporta coordenadas extremas.
- Una segunda detonación responde `409` y no modifica nuevamente las víctimas.
- La suite completa ejecuta 52 pruebas sin fallos.
