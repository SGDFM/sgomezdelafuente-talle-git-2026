# Bitacora

## Asistente utilizado

- Herramienta: OpenCode / ChatGPT.
- Modelo informado por la herramienta: `openai/gpt-5.5`.


## Resumen de prompts usados

- Revisar el checklist del taller y comparar el repositorio con lo que faltaba entregar.
- Ordenar primero las tareas de base: estado de Git, repositorio remoto, Java 21 y Maven.
- Verificar que el proyecto Spring Boot compilara antes de incorporar nuevas clases del dominio.
- Usar el diagrama Mermaid de Minecraft como referencia para armar el modelo inicial con `Entidad`, `Mob`, `Jugador`, `Creeper`, `Zombie`, `ZombiePequeno`, `Cerdo` y `Aldeano`.
- Revisar si el modelo solo copiaba el diagrama o si tambien protegia el estado interno de las entidades.
- Ajustar la jerarquia para que las reglas comunes no quedaran repetidas en cada clase hija.
- Endurecer el modelo para que cualquier `Entidad` pudiera moverse, recibir dano y desaparecer sin preguntar por su tipo concreto.
- Agregar `Esqueleto` como una especializacion adicional para ampliar el dominio sin romper la jerarquia existente.
- Revisar que el controller no asignara campos directamente y que usara constructores y metodos del dominio.
- Agregar los servicios REST minimos: `GET /` y un controller que construye entidades desde parametros de URL.
- Probar la respuesta JSON del endpoint de comportamientos y ajustar el diseno para que el controller usara el tipo padre `Entidad`.
- Reorganizar las carpetas siguiendo `lp3-template-tp`: `domain` para el modelo, `rest/controller` para HTTP y `Application` como punto de entrada.
- Agregar constructores simples y sobrecargados en clases del dominio para crear objetos validos con distintas firmas.
- Agregar sobrecarga del mensaje `disparar` en `Esqueleto`, diferenciando el disparo simple del disparo con distancia.
- Revisar la sobreescritura en `comportamiento()`, `recibirDano()` y `desaparecer()` para distinguirla de la sobrecarga.
- Actualizar el README con el diagrama Mermaid alineado a `src/` y con una explicacion corta de sobrecarga y sobreescritura.
- Armar una entrega en Markdown para Classroom con objetivo, consignas aplicadas al dominio y forma de probar el servicio.
- Revisar el resultado con la rubrica para mirar paquetes, ocultamiento, herencia, constructores, Git, documentacion y comportamiento observable.
- Validar con Maven y dejar registrado el commit exacto que representa la solucion revisable.

## Validaciones realizadas

Durante el proceso se usaron principalmente estos comandos:

```bash
./mvnw test
./mvnw spring-boot:run
git status
git log --oneline
```

Resultado esperado final:

- Tests pasando.
- API levantando en `http://localhost:8080`.
- `GET /` disponible.
- Endpoint de comportamientos devolviendo JSON.
- README alineado con el codigo.


