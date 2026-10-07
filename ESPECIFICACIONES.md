# Especificaciones del ejercicio

## Objetivo

Commit de solucion POO-06: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026/commit/dfbdf519b86dd60384a51b330bc2a3fc711428a4

Implementar una API REST Spring Boot que exponga un modelo orientado a objetos del dominio Minecraft, aplicando encapsulamiento, herencia, polimorfismo y validacion de invariantes.

## Dominio

Minecraft.

La clase base es `Entidad`. Las especializaciones principales son `Jugador`, `Mob`, `Zombie`, `ZombiePequeno`, `Creeper`, `Esqueleto`, `Cerdo` y `Aldeano`.

Las clases estan organizadas semanticamente siguiendo el template de LP3:

- `py.edu.uc.lp3.domain.minecraft`: clases del dominio.
- `py.edu.uc.lp3.rest.controller`: controllers REST.
- `py.edu.uc.lp3.Application`: clase principal Spring Boot.

## Consignas cubiertas

- Proyecto Spring Boot Maven con Java 21.
- API REST con `IndexController` en `GET /`.
- Controller de dominio en `/minecraft/sgomezdelafuente`.
- Clase base abstracta `Entidad` con método abstracto `comportamiento()`.
- Hijas independientes que implementan comportamiento propio.
- Controller que pide comportamiento usando el tipo padre `Entidad`.
- Constructores simples y sobrecargados en `Jugador`, `Creeper` y `Esqueleto`.
- Sobrecarga del mensaje `Esqueleto.disparar(...)`.
- Estado encapsulado con atributos `private`.
- Reglas de validacion dentro de las clases.
- README con diagrama Mermaid alineado al codigo.

## Criterios de aceptacion

- El proyecto compila con Maven.
- La API levanta en `http://localhost:8080`.
- El estado no puede quedar invalido desde el controller.
- No hay asignacion directa a campos desde fuera de las clases.
- El endpoint de comportamientos no pregunta el tipo concreto para armar el mensaje.
- Los constructores sobrecargados dejan siempre objetos validos.
- El metodo sobrecargado `disparar` consume flechas y valida distancia.

## Como probar

```bash
./mvnw test
./mvnw spring-boot:run
```

Endpoints sugeridos:

```text
GET http://localhost:8080/
GET http://localhost:8080/minecraft/sgomezdelafuente/comportamientos?vida=20&velocidad=2&cargaExplosion=3&flechas=5&comercializacion=true
GET http://localhost:8080/minecraft/sgomezdelafuente/creeper
GET http://localhost:8080/minecraft/sgomezdelafuente/jugador
```

## Revision pedida

Verificar si desde el controller o desde otra clase se puede romper algun invariante del dominio, especialmente vida negativa, carga de explosion negativa, flechas negativas o armadura negativa.
