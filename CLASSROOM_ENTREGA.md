# Entrega Classroom - POO-08 Minecraft

Alumno: Sebastián Gómez de la Fuente

Usuario GitHub: SGDFM

Repositorio: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026

Commit de solución POO-08: se agrega en el siguiente commit documental.

## Objetivo

Publicar un servicio HTTP Spring Boot con arquitectura de tres capas que persiste una familia polimórfica de Minecraft en H2. La aplicación incluye CRUD REST, validaciones de negocio, una interfaz de servicio, una simulación de explosiones y una jerarquía de excepciones con respuestas HTTP diferenciadas.

## Clases persistidas

- `Jugador`.
- `Creeper`.
- `Zombie`.

Las tres pertenecen a la familia de `Entidad`. La estrategia JPA es `SINGLE_TABLE` y la tabla usa una columna discriminadora para reconocer el subtipo.

## Arquitectura

```text
EntidadController / SimulacionController
                 |
                 v
EntidadService / JuegoService / ExplosionService
                 |
                 v
EntidadRepository
                 |
                 v
                H2
```

Los controladores traducen HTTP. Los servicios validan, ejecutan las reglas y coordinan transacciones. El repositorio traduce entidades a filas. Las entidades JPA conservan únicamente estado y no lanzan excepciones.

## URLs

```text
GET    /
POST   /minecraft/sgomezdelafuente/entidades
GET    /minecraft/sgomezdelafuente/entidades
GET    /minecraft/sgomezdelafuente/entidades/{id}
PUT    /minecraft/sgomezdelafuente/entidades/{id}
DELETE /minecraft/sgomezdelafuente/entidades/{id}
GET    /minecraft/sgomezdelafuente/entidades/ui
POST   /minecraft/sgomezdelafuente/simulaciones/explosion
```

## Invariantes

- La vida inicial debe ser mayor que cero.
- Daño base, velocidad, armadura y carga de explosión no pueden ser negativos.
- Solo `JUGADOR`, `CREEPER` y `ZOMBIE` entran en el CRUD persistido.
- Un PUT no puede cambiar el subtipo de una entidad existente.
- El Creeper debe estar vivo y tener al menos tres puntos de carga para detonar.
- El daño de explosión solo alcanza entidades vivas dentro del radio indicado.
- Un POST inválido no agrega una fila a H2.

## Interfaz de servicio

`UICapable` declara `String describirUI(Entidad entidad)`. `JuegoService` extiende esa interfaz y `JuegoServiceImpl` produce como texto la imagen, la posición, el sonido y la velocidad. El endpoint `/entidades/ui` solicita el mensaje mediante el contrato `UICapable` heredado por `JuegoService` y responde con un JSON, sin colocar presentación en las entidades.

## Reglas de la simulación

1. Un Creeper vivo con carga mínima puede detonar; el daño es cuatro veces su carga.
2. El Jugador y el Zombie reciben ese daño solo dentro del radio; el servicio reduce el daño del Jugador según su armadura.

El Creeper desaparece y los tres participantes quedan actualizados en H2 dentro de una sola transacción.

## Excepciones

```text
EntidadNoEncontradaException   -> 404
EstadoEntidadInvalidoException -> 400
ExplosionNoPermitidaException  -> 409
```

Cada clase hereda de `MinecraftException` y tiene un método manejador HTTP propio.

## H2

```properties
spring.datasource.url=jdbc:h2:file:./data/entidades
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.h2.console.enabled=true
```

Consola:

```text
http://localhost:8080/h2-console
```

## Cómo probar

```bash
./mvnw test
./mvnw spring-boot:run
```

Importar en Insomnia:

```text
insomnia/poo-08-minecraft.json
```

Recorrido recomendado:

1. Crear Creeper, Jugador y Zombie.
2. Copiar los IDs a las variables del entorno de Insomnia.
3. Listar y buscar los recursos.
4. Actualizar el Creeper y comprobar el cambio con GET.
5. Consultar el endpoint de `UICapable`.
6. Ejecutar la simulación de explosión.
7. Hacer otro GET para verificar el estado persistido.
8. Ejecutar POST inválido, ID inexistente y explosión sin carga.
9. Borrar un recurso y verificar el `404` posterior.

## Evidencias esperadas

- POST responde `201` y un `id` generado.
- GET lista datos recuperados desde H2.
- PUT conserva el cambio en un GET posterior.
- DELETE responde `204`.
- La simulación modifica las tres entidades y el cambio permanece.
- Cada excepción devuelve un JSON con un tipo y un código diferentes.
- La tabla `ENTIDADES` puede verse desde la consola H2.
