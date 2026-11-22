# Entrega Classroom - POO-09 Minecraft

Alumno: Sebastián Gómez de la Fuente

Usuario GitHub: SGDFM

Repositorio: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026

Commit de solución POO-09 corregida: pendiente de commit local; no publicado.

## Objetivo

Publicar la API propia mediante OpenAPI y Swagger UI, consumir con `WebClient` las APIs REST de dos compañeros y resolver un escenario coordinado. En esta entrega Sebastián ocupa el rol A1 y persiste la jugada en su H2 únicamente cuando A2 y A3 responden exitosamente.

## Integrantes y responsabilidades

| Rol | Integrante | Repositorio | Responsabilidad integrada |
|---|---|---|---|
| A1 | Sebastián Gómez de la Fuente | https://github.com/SGDFM/sgomezdelafuente-talle-git-2026 | Orquestación, respuesta HTTP y persistencia local. |
| A2 | Ruth Santacruz | https://github.com/ruthsantacruzuca/rsantacruz-taller-git-2026 | Explosión y reacción en cadena. |
| A3 | Matías Duarte | https://github.com/Cemelele/mfdc-taller-git-2026 | Radio, víctimas y ciclo de vida del Creeper. |

Ruth publica POO-09 y OpenAPI en `main`, commit `fd6aebe`. Matías mantiene su avance POO-09 y OpenAPI en `feature/RESTClient`, commit `f3d717a`, todavía sin fusionar a `main`; el endpoint de explosión consumido por A1 sí está disponible en su rama principal. Los adaptadores permiten cambiar URL y ruta mediante configuración.

## Contrato propio

```text
GET  /v3/api-docs
GET  /swagger-ui.html
POST /minecraft/sgomezdelafuente/escenarios/explosiones
GET  /minecraft/sgomezdelafuente/escenarios/explosiones
```

El POST coordinado puede responder:

| Estado | Significado |
|---|---|
| `201` | A2 y A3 aceptaron; A1 persistió la jugada. |
| `400` | El pedido local no tiene los datos obligatorios. |
| `422` | A2 o A3 rechazó su regla; la respuesta identifica al compañero. |
| `502` | No fue posible contactar al compañero identificado. |

## APIs consumidas

```text
A2 Ruth:   POST http://localhost:8081/api/minecraft/simulaciones/explosion
A3 Matías: POST http://localhost:8082/api/simulaciones/explosion
```

Variables admitidas:

```text
RUTH_BASE_URL
RUTH_EXPLOSION_PATH
MATIAS_BASE_URL
MATIAS_EXPLOSION_PATH
```

## Ejecución

Iniciar A2, A3 y A1 en puertos diferentes:

```bash
# A2 Ruth
SERVER_PORT=8081 ./mvnw spring-boot:run

# A3 Matías
SERVER_PORT=8082 ./mvnw spring-boot:run

# A1 Sebastián
./mvnw spring-boot:run
```

A2 y A3 deben tener previamente Creepers y víctimas válidos en sus bases. Los IDs se colocan en la colección Insomnia.

## Camino feliz

```json
{
  "descripcion": "Explosión coordinada en una mina",
  "ruth": {
    "creeperId": 1
  },
  "matias": {
    "creeperId": 1,
    "cargado": true,
    "encendido": true,
    "victimas": [
      {
        "hostilId": 2,
        "distancia": 2.0
      }
    ]
  }
}
```

Secuencia:

1. A1 valida el pedido.
2. A1 consulta a Ruth (A2) mediante `WebClient`.
3. Si Ruth acepta, A1 consulta a Matías (A3).
4. Si Matías acepta, `JugadaCruzadaRepository` inserta la jugada en la H2 de A1.
5. A1 responde `201` con el ID generado.

## Rechazos y persistencia

Un error remoto se convierte en `IntegracionErrorResponse`, que conserva el estado y cuerpo remotos e identifica `A2 - Ruth Santacruz` o `A3 - Matías Duarte`. Si A2 rechaza, A3 no es consultado. Si cualquiera rechaza, A1 no inserta una fila.

La garantía transaccional corresponde a la H2 de A1. No hay una transacción distribuida que pueda revertir un efecto ya realizado dentro de A2 antes de un eventual rechazo de A3.

La interoperabilidad fue comprobada levantando simultáneamente A2 `fd6aebe` en `8081`, A3 `e24255e` en `8082` y A1 en `8080`, con bases H2 en memoria. El camino feliz respondió `201`; los rechazos reales de A2 y A3 respondieron `422`, identificaron al compañero y mantuvieron una sola fila aceptada en A1. Al detener A2, A1 respondió `502`, identificó a Ruth y tampoco agregó una fila.

## Logging

Log4j2 conserva el nivel raíz en `INFO` y habilita `DEBUG` para los clientes de integración y `EscenarioCruzadoServiceImpl`. Las trazas muestran inicio, compañero consultado, estado y cuerpo remoto, aceptación y persistencia local. Los clientes limitan la conexión a 3 segundos y la respuesta a 5 segundos.

## Insomnia

Importar:

```text
insomnia/poo-09-integracion.json
```

La colección contiene:

1. Consulta del documento OpenAPI.
2. Escenario aceptado por A2 y A3.
3. Rechazo de Ruth (A2) con un Creeper inexistente.
4. Rechazo de Matías (A3) con un Creeper inexistente.
5. GET de jugadas aceptadas y persistidas.

Antes de ejecutar se deben actualizar los IDs del entorno con registros reales de A2 y A3. Cada explosión puede cambiar el estado remoto; para repetir una evidencia se deben preparar nuevos datos.

## Arquitectura

```mermaid
flowchart LR
    Cliente --> Controller[EscenarioCruzadoController]
    Controller --> Service[EscenarioCruzadoServiceImpl]
    Service --> MatiasClient[WebClient Matías]
    Service --> RuthClient[WebClient Ruth]
    RuthClient --> A2[API A2 Ruth]
    MatiasClient --> A3[API A3 Matías]
    Service --> Repository[JugadaCruzadaRepository]
    Repository --> H2[(H2 A1)]
```

```mermaid
sequenceDiagram
    participant C as Cliente
    participant A1 as A1 Sebastián
    participant A2 as A2 Ruth
    participant A3 as A3 Matías
    participant DB as H2 A1
    C->>A1: POST escenario
    A1->>A2: POST explosión
    A2-->>A1: 2xx
    A1->>A3: POST explosión
    A3-->>A1: 2xx
    A1->>DB: INSERT jugada
    A1-->>C: 201 Created
```

## Pruebas

```bash
./mvnw test
```

Resultado: 52 pruebas, 0 fallos y 0 errores. Además del camino feliz, OpenAPI y los rechazos A2/A3, se prueban 15 entradas adversariales de Creeper, overflow aritmético, posición conservada, tipos incorrectos, IDs inválidos, carga insuficiente, segunda detonación y ausencia de persistencia parcial.
