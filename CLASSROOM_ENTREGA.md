# Entrega Classroom - POO-08 Minecraft

Alumno: Sebastian Gomez de la Fuente

Usuario GitHub: SGDFM

Repositorio: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026

Commit de solucion POO-08: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026/commit/9493e2f4ea2c9bd9f48b96278462901e9571da2a

## Objetivo

Publicar un servicio HTTP Spring Boot con arquitectura de tres capas que persiste una familia polimorfica de Minecraft en H2. La aplicacion incluye CRUD REST, validaciones de negocio, una interfaz de dominio, una simulacion de explosiones y una jerarquia de excepciones con respuestas HTTP diferenciadas.

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
EntidadService / ExplosionService
                 |
                 v
EntidadRepository
                 |
                 v
                H2
```

Los controllers traducen HTTP. Los services validan y coordinan transacciones. El repositorio traduce entidades a filas. Las reglas de juego permanecen en el dominio.

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
- Dano base, velocidad, armadura y carga de explosion no pueden ser negativos.
- Solo `JUGADOR`, `CREEPER` y `ZOMBIE` entran en el CRUD persistido.
- Un PUT no puede cambiar el subtipo de una entidad existente.
- El Creeper debe estar vivo y tener al menos tres puntos de carga para detonar.
- El dano de explosion solo alcanza entidades vivas dentro del radio indicado.
- Un POST invalido no agrega una fila a H2.

## Interface De Dominio

`UICapable` declara `String describirUI()`. Las entidades concretas responden con imagen, posicion, sonido y velocidad como texto. El endpoint `/entidades/ui` solicita el mensaje usando el tipo de la interfaz y responde JSON.

## Reglas De La Simulacion

1. Un Creeper vivo con carga minima puede detonar; el dano es cuatro veces su carga.
2. Jugador y Zombie reciben ese dano solo dentro del radio. El Jugador descuenta armadura mediante su sobreescritura de `recibirDano()`.

El Creeper desaparece y los tres participantes quedan actualizados en H2 dentro de una sola transaccion.

## Excepciones

```text
EntidadNoEncontradaException   -> 404
EstadoEntidadInvalidoException -> 400
ExplosionNoPermitidaException  -> 409
```

Cada clase hereda de `MinecraftException` y tiene un handler HTTP propio.

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

## Como Probar

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
6. Ejecutar la simulacion de explosion.
7. Hacer otro GET para verificar el estado persistido.
8. Ejecutar POST invalido, ID inexistente y explosion sin carga.
9. Borrar un recurso y verificar el `404` posterior.

## Evidencias Esperadas

- POST responde `201` y un `id` generado.
- GET lista datos recuperados desde H2.
- PUT conserva el cambio en un GET posterior.
- DELETE responde `204`.
- La simulacion modifica las tres entidades y el cambio permanece.
- Cada excepcion devuelve JSON con tipo y codigo diferente.
- La tabla `ENTIDADES` puede verse desde la consola H2.
