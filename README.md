# sgomezdelafuente-talle-git-2026

Nombre: Sebastian Gomez de la Fuente
Usuario GitHub: SGDFM
Comision: CYT646 F

## Como levantar la API

```bash
./mvnw spring-boot:run
```

## Endpoints principales

- `GET /`: estado basico de la API.
- `GET /minecraft/sgomezdelafuente/comportamientos?vida=20&velocidad=2&cargaExplosion=3&comercializacion=true`: construye dos entidades y devuelve su comportamiento usando el tipo padre `Entidad`.
- `GET /minecraft/sgomezdelafuente/creeper`: estado del creeper.
- `GET /minecraft/sgomezdelafuente/jugador`: estado del jugador.

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
        +Creeper(int vida, int danoBase, int velocidad, int cargaExplosion)
        +int getCargaExplosion()
        +void setCargaExplosion(int cargaExplosion)
        +void aumentarCarga(int cantidad)
        +void explotar()
        +void desaparecer()
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
    Mob <|-- Cerdo
    Mob <|-- Aldeano
    Zombie <|-- ZombiePequeno
    Zombie --> Jugador : ataca
```
