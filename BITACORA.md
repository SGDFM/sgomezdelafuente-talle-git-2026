# Bitácora de asistencia de IA

## Asistente utilizado

- Herramienta: OpenCode / ChatGPT.
- Modelo usado en POO-08: `openai/gpt-5.6-sol`.

## Resumen de prompts usados

- Incorporar un CRUD de `Entidad`, arquitectura de tres capas, validación en servicios, H2 y ejemplos con curl.
- Elegir `Jugador`, `Creeper` y `Zombie` como clases persistidas de la familia `Entidad`.
- Configurar H2 en archivo.
- Adaptar la jerarquía a JPA sin permitir que Jackson escriba directamente sobre las entidades.
- Crear un repositorio Spring Data y una capa de servicios responsable de validaciones y transacciones.
- Implementar el CRUD REST con discriminación de subtipo mediante el campo `tipo`.
- Hacer que el servicio describa imagen, posición, sonido y velocidad usando solamente texto.
- Agregar un endpoint que pida `describirUI()` a través del tipo de la interfaz.
- Definir dos reglas de explosión: carga mínima del Creeper y daño limitado por radio.
- Crear una simulación persistida con `Creeper`, `Jugador` y `Zombie`.
- Crear una jerarquía de excepciones con manejadores HTTP diferentes para entidad inexistente, estado inválido y explosión no permitida.
- Preparar pruebas de dominio, persistencia, CRUD, simulación y respuestas de error.
- Crear una colección de Insomnia con el flujo exitoso y las solicitudes que provocan cada excepción.
- Corregir la separación de responsabilidades para que las entidades solo conserven estado y los servicios ejecuten todas las reglas.
- Revisar la ortografía y la gramática de toda la documentación entregada.


## Validaciones realizadas

```bash
./mvnw test
./mvnw spring-boot:run
```
