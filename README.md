# sgomezdelafuente-talle-git-2026

Nombre: Sebastian Gomez de la Fuente
Usuario GitHub: SGDFM
Comision: CYT646 F

Commit de solucion POO-06: https://github.com/SGDFM/sgomezdelafuente-talle-git-2026/commit/7ae547ed8c618e43e0bf834c34d81a051fbbb7c6

## Como levantar la API

```bash
./mvnw spring-boot:run
```

## Endpoints principales

- `GET /`: estado basico de la API.
- `GET /minecraft/sgomezdelafuente/comportamientos?vida=20&velocidad=2&cargaExplosion=3&flechas=5&comercializacion=true`: construye entidades y devuelve su comportamiento usando el tipo padre `Entidad`.
- `GET /minecraft/sgomezdelafuente/creeper`: estado del creeper.
- `GET /minecraft/sgomezdelafuente/jugador`: estado del jugador.

## Estructura semantica

```text
src/main/java/py/edu/uc/lp3/
├── Application.java
├── domain/minecraft/       # modelo de dominio
└── rest/controller/        # controllers REST
```

## Sobrecarga y sobreescritura

Sobrecarga agregada:

- `Creeper()` y `Creeper(int cargaExplosion)` delegan en `Creeper(int vida, int danoBase, int velocidad, int cargaExplosion)`.
- `Esqueleto()` y `Esqueleto(int flechas)` delegan en `Esqueleto(int vida, int danoBase, int velocidad, int flechas)`.
- `Jugador()` delega en `Jugador(int vida, int danoBase, int velocidad, int armadura)`.
- `Esqueleto.disparar(Jugador jugador)` aplica el dano base.
- `Esqueleto.disparar(Jugador jugador, int distancia)` representa el mismo mensaje del dominio con otro contexto: si la distancia es mayor a 10, el dano baja.

Sobreescritura aplicada:

- `Entidad` declara `comportamiento()` como metodo abstracto.
- `Creeper`, `Aldeano` y `Esqueleto` lo implementan con la misma firma y comportamiento propio.
- `Jugador` sobrescribe `recibirDano(int dano)` para aplicar armadura antes de modificar la vida.
- `Creeper` sobrescribe `desaparecer()` para reiniciar su carga de explosion cuando muere.

La diferencia principal es que la sobrecarga mantiene el mismo nombre con parametros distintos dentro de una clase, mientras que la sobreescritura mantiene la misma firma heredada y cambia la implementacion en una subclase.

## Diagrama de clases

```mermaid
classDiagram
    class Entidad {
        <<abstract>>
        -int vida
        -int danoBase
        -int velocidad
        -int x
        -int y
        +Entidad(int vida, int danoBase, int velocidad)
        +int getVida()
        +void setVida(int vida)
        +int getDanoBase()
        +void setDanoBase(int danoBase)
        +int getVelocidad()
        +void setVelocidad(int velocidad)
        +void moverse(int deltaX, int deltaY)
        +void recibirDano(int dano)
        +void desaparecer()
        +boolean estaViva()
        +String comportamiento()*
    }

    class Jugador {
        -int armadura
        +Jugador()
        +Jugador(int vida, int danoBase, int velocidad, int armadura)
        +int getArmadura()
        +void setArmadura(int armadura)
        +void setVida(int vida)
        +void recibirDano(int dano)
        +void curar(int cantidad)
        +String comportamiento()
    }

    class Mob {
        <<abstract>>
        -boolean hostil
        +Mob(int vida, int danoBase, int velocidad, boolean hostil)
        +boolean isHostil()
        +void setHostil(boolean hostil)
    }

    class Zombie {
        +Zombie(int vida, int danoBase, int velocidad)
        +void atacar(Jugador jugador)
        +String comportamiento()
    }

    class ZombiePequeno {
        -int velocidadAumentada
        +ZombiePequeno(int vida, int danoBase, int velocidad, int velocidadAumentada)
        +int getVelocidadAumentada()
        +void setVelocidadAumentada(int velocidadAumentada)
        +int getVelocidadTotal()
        +String comportamiento()
    }

    class Creeper {
        -int cargaExplosion
        +Creeper()
        +Creeper(int cargaExplosion)
        +Creeper(int vida, int danoBase, int velocidad, int cargaExplosion)
        +int getCargaExplosion()
        +void setCargaExplosion(int cargaExplosion)
        +void aumentarCarga(int cantidad)
        +void explotar()
        +void desaparecer()
        +String comportamiento()
    }

    class Esqueleto {
        -int flechas
        +Esqueleto()
        +Esqueleto(int flechas)
        +Esqueleto(int vida, int danoBase, int velocidad, int flechas)
        +int getFlechas()
        +void setFlechas(int flechas)
        +void disparar(Jugador jugador)
        +void disparar(Jugador jugador, int distancia)
        +void recargar(int cantidad)
        +String comportamiento()
    }

    class Cerdo {
        -boolean montable
        +Cerdo(int vida, int danoBase, int velocidad, boolean montable)
        +boolean isMontable()
        +void setMontable(boolean montable)
        +String comportamiento()
    }

    class Aldeano {
        -boolean comercializacion
        +Aldeano(int vida, int danoBase, int velocidad, boolean comercializacion)
        +boolean puedeComercializar()
        +void setComercializacion(boolean comercializacion)
        +String comportamiento()
    }

    Entidad <|-- Jugador
    Entidad <|-- Mob
    Mob <|-- Zombie
    Mob <|-- Creeper
    Mob <|-- Esqueleto
    Mob <|-- Cerdo
    Mob <|-- Aldeano
    Zombie <|-- ZombiePequeno
    Zombie --> Jugador : ataca
    Esqueleto --> Jugador : dispara
```
