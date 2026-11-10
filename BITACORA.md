# Bitacora de asistencia de IA

## Asistente utilizado

- Herramienta: OpenCode / ChatGPT.
- Modelo usado en POO-08: `openai/gpt-5.6-sol`.

## Resumen de prompts usados

- Incorporar: CRUD de `Entidad`, arquitectura de tres capas, validacion en service, H2 y ejemplos con curl.
- Elegir `Jugador`, `Creeper` y `Zombie` como clases persistidas de la familia `Entidad`.
- Configurar H2 en archivo.
- Adaptar la jerarquia a JPA sin permitir que Jackson escriba directamente sobre las entidades.
- Crear un repositorio Spring Data y una capa service responsable de validaciones y transacciones.
- Implementar el CRUD REST con discriminacion de subtipo mediante el campo `tipo`.
- Hacer que los elementos dibujables describan imagen, posicion, sonido y velocidad usando solamente texto.
- Agregar un endpoint que pida `describirUI()` a traves del tipo de la interfaz.
- Definir dos reglas de explosion: carga minima del Creeper y dano limitado por radio.
- Crear una simulacion persistida con `Creeper`, `Jugador` y `Zombie`.
- Crear una jerarquia de excepciones con handlers HTTP diferentes para entidad inexistente, estado invalido y explosion no permitida.
- Preparar pruebas de dominio, persistencia, CRUD, simulacion y respuestas de error.
- Crear una coleccion Insomnia con el recorrido feliz y los pedidos que provocan cada excepcion.


## Validaciones realizadas

```bash
./mvnw test
./mvnw spring-boot:run
```

