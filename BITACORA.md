# Bitácora de asistencia de IA

## Asistente utilizado

- Herramienta: OpenCode / ChatGPT.
- Modelo usado en POO-08 y POO-09: `openai/gpt-5.6-sol`.

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
- Publicar todos los controladores mediante OpenAPI y Swagger UI.
- Aplicar roles relativos a esta entrega: Sebastián como A1, Ruth Santacruz como A2 y Matías Duarte como A3.
- Revisar la entrega POO-09 publicada por Ruth y la rama `feature/RESTClient` de Matías.
- Crear clientes `WebClient` con URLs configurables para consultar las explosiones de A2 y A3.
- Orquestar desde A1 un escenario que consulta primero A2, luego A3 y persiste en H2 solo cuando ambos aceptan.
- Diferenciar un rechazo remoto (`422`) de un servicio no disponible (`502`) e identificar al compañero en la respuesta.
- Registrar los pasos de integración con Log4j2 en nivel `DEBUG`.
- Probar el camino feliz, un rechazo por compañero, la indisponibilidad y la ausencia de persistencia ante errores.
- Crear una colección Insomnia y diagramas Mermaid para la integración POO-09.
- Levantar A1, A2 y A3 simultáneamente en la VM y comprobar un camino feliz y un rechazo real por compañero.
- Auditar entradas adversariales de Creeper y corregir overflow de carga, overflow de distancia, posición en PUT y coerción de JSON.
- Ampliar la suite a 52 pruebas, con 15 casos adversariales específicos de Creeper.


## Validaciones realizadas

```bash
./mvnw test
./mvnw spring-boot:run
```
