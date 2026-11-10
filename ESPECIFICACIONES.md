# Especificaciones POO-08

Este archivo resume las especificaciones aplicadas al dominio Minecraft. La entrega completa para Classroom se encuentra en `CLASSROOM_ENTREGA.md`.

Commit de solucion POO-08: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026/commit/9493e2f4ea2c9bd9f48b96278462901e9571da2a

## Alcance

- Persistencia H2 de `Jugador`, `Creeper` y `Zombie`.
- CRUD REST de la familia `Entidad`.
- Arquitectura controller, service y repository.
- Interfaz `UICapable` usada por un endpoint de lectura.
- Simulacion persistida de explosiones con dos reglas.
- Excepciones de dominio y handlers por tipo.
- Coleccion Insomnia versionada.

## Aceptacion

- `./mvnw test` termina correctamente.
- `./mvnw spring-boot:run` levanta la API.
- POST, GET, PUT y DELETE trabajan contra H2.
- Un POST ilegal no altera la cantidad de filas.
- Un GET posterior confirma el resultado de la simulacion.
- Los tres errores del dominio producen estados HTTP distinguibles.
- `insomnia/poo-08-minecraft.json` permite reproducir el recorrido.
