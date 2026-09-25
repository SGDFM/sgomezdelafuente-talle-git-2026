# Entrega Classroom - Taller Git 2026

Repositorio: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026

Alumno: Sebastian Gomez de la Fuente  
Usuario GitHub: SGDFM  
Dominio elegido: Minecraft

## Bitacora de trabajo

### 1. Creacion del repositorio

Se creo un repositorio publico en GitHub para el taller:

```text
sgomezdelafuente-talle-git-2026
```

El repositorio incluye:

- `README.md` inicial.
- Licencia Apache License 2.0.
- Visibilidad publica.

Luego se trabajo localmente con Git y se mantuvo el codigo sincronizado con GitHub.

### 2. Incorporacion del proyecto Spring Boot

Se incorporo un proyecto Spring Boot generado desde `start.spring.io` con las siguientes caracteristicas:

- Maven.
- Java 21.
- Spring Web para API REST.
- Packaging JAR.
- Grupo base `py.edu.uc.lp3`.

Se verifico que el proyecto compile y que la API arranque correctamente con:

```bash
./mvnw spring-boot:run
```

La aplicacion levanta en:

```text
http://localhost:8080
```

Tambien se verifico con:

```bash
./mvnw test
```

### 3. Modelado Minecraft

Se incorporaron las clases del dominio Minecraft dentro del paquete:

```text
src/main/java/py/edu/uc/lp3/minecraft
```

Clases principales:

- `Entidad`
- `Jugador`
- `Mob`
- `Zombie`
- `ZombiePequeno`
- `Creeper`
- `Esqueleto`
- `Cerdo`
- `Aldeano`

El modelo aplica:

- Encapsulamiento con atributos `private`.
- Herencia entre entidades.
- Metodo abstracto en la clase base.
- Validacion de invariantes en las clases del dominio.
- Comportamiento polimorfico sin preguntar el tipo concreto en el controller.

Despues de copiar y adaptar las clases, se corrigieron paquetes e imports hasta que Maven compilara correctamente.

### 4. Controllers REST

Se agrego un `IndexController` para responder en la raiz de la API:

```text
GET /
```

Respuesta esperada:

```text
API Minecraft - SGDFM
```

Tambien se agrego un controller de dominio:

```text
/minecraft/sgomezdelafuente
```

Endpoints principales:

```text
GET /minecraft/sgomezdelafuente/creeper
GET /minecraft/sgomezdelafuente/jugador
GET /minecraft/sgomezdelafuente/comportamientos?vida=20&velocidad=2&cargaExplosion=3&flechas=5&comercializacion=true
```

El endpoint de comportamientos construye instancias del dominio usando parametros de la URL y devuelve JSON.

### 5. Metodo abstracto y polimorfismo

La clase base `Entidad` declara el metodo abstracto:

```java
public abstract String comportamiento();
```

Cada hija implementa su comportamiento especifico. Por ejemplo:

- `Creeper`: representa un mob hostil que aumenta carga y explota.
- `Aldeano`: representa un mob no hostil que puede comerciar.
- `Esqueleto`: representa un mob hostil que dispara flechas.

El controller trabaja con el tipo padre:

```java
Entidad primera = new Creeper(...);
Entidad segunda = new Aldeano(...);
Entidad tercera = new Esqueleto(...);
```

Luego pide el mensaje comun:

```java
entidad.comportamiento()
```

De esta forma, el controller no necesita preguntar si la entidad es `Creeper`, `Aldeano` o `Esqueleto` para obtener su comportamiento.

### 6. README con diagrama Mermaid

El `README.md` del repositorio incluye un diagrama Mermaid actualizado con las clases del dominio Minecraft.

El diagrama muestra:

- Clase base `Entidad`.
- Clase intermedia `Mob`.
- Especializaciones del dominio.
- Relaciones de herencia.
- Relacion de ataque o interaccion hacia `Jugador`.

### 7. Colaboracion

Se realizo una colaboracion sobre el repositorio de una companera:

```text
https://github.com/ruthsantacruzuca/rsantacruz-taller-git-2026
```

Se creo una rama de contribucion:

```text
sgdfm-contribucion-lp3
```

Se agrego una especializacion `Esqueleto` como mob hostil, con validacion de precision de arco, integracion al factory y tests.

Pull request abierto:

```text
https://github.com/ruthsantacruzuca/rsantacruz-taller-git-2026/pull/1
```

### 8. Verificacion final

Comandos utilizados para validar el proyecto:

```bash
./mvnw test
./mvnw spring-boot:run
```

Resultado esperado:

- Los tests pasan sin errores.
- La API levanta en el puerto 8080.
- `GET /` responde correctamente.
- El endpoint de comportamientos devuelve JSON con comportamientos polimorficos.

## Enlace final del repositorio

```text
https://github.com/SGDFM/sgomezdelafuente-talle-git-2026
```
