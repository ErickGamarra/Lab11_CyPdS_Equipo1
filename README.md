# Spring Boot : Application PetClinic

## Features

### 1.  Unit Test  --> Tag v1.0.0

mvn test -Dspring.profiles.active=h2

## Laboratorio 11 · Grupo 1 · OwnerService

Jose Contreras Cabrera (integrante A) implementó las pruebas de creación, actualización y eliminación en `OwnerServiceTest.java`. Las pruebas comprueban los datos en H2 y usan Mockito para verificar las llamadas al repositorio. Cada prueba revierte sus cambios al finalizar.

| Prueba | Comprobación |
| --- | --- |
| `testCreateOwner` | El servicio guarda al dueño y devuelve sus datos con un ID asignado. |
| `testUpdateOwner` | La ciudad y el teléfono cambian en la fila con el ID original. |
| `testDeleteOwner` | El ID existe antes de eliminar al dueño y desaparece de H2 después. |

Se incorporó la base de OwnerService de Jesús Rocha (integrante B), con sus pruebas de búsqueda. Esta integración en la rama A se realizó sin conflictos.

Para ejecutar la suite en Windows: `.\mvnw.cmd test "-Dspring.profiles.active=h2"`.

## Resumen del equipo · Grupo 1 · OwnerService

| Integrante | Rama | Pruebas |
| --- | --- | --- |
| A · José Contreras Cabrera | `Lab11-IntegranteA-Contreras` | `testCreateOwner`, `testUpdateOwner`, `testDeleteOwner` |
| B · Jesús Rocha | `Lab11-IntegranteB-Rocha` | `testFindOwnerById`, `testFindByLastName`, `testFindByCity` |
| C · Erick Gamarra | `Lab11-IntegranteC-Gamarra` | `testFindOwnerById_NotFound`, `testDeleteOwner_NotFound`, `testFindByLastName_Empty` |

Las 9 pruebas están en `src/test/java/com/tecsup/petclinic/services/OwnerServiceTest.java` y usan H2 con `@SpringBootTest` y `@MockitoSpyBean` para verificar las llamadas al repositorio.

### Pruebas del integrante C (casos de error)

| Prueba | Comprobación |
| --- | --- |
| `testFindOwnerById_NotFound` | Buscar el ID 666, que no existe, lanza `OwnerNotFoundException` con el mensaje esperado. |
| `testDeleteOwner_NotFound` | Borrar el ID 1000 lanza `OwnerNotFoundException`, no llama a `delete` y la cantidad de dueños no cambia. |
| `testFindByLastName_Empty` | Buscar un apellido que no existe devuelve una lista vacía, no `null`. |

### Conflicto resuelto

- **Dónde:** al unir la rama `Lab11-IntegranteA-Contreras` (que ya traía la base de Rocha) en `Lab11-IntegranteC-Gamarra`.
- **Archivos:** `Owner`, `OwnerNotFoundException`, `OwnerRepository`, `OwnerService`, `OwnerServiceImpl` y `OwnerServiceTest`.
- **Causa:** el integrante C había creado su propia base de Owner con `OwnerDTO` y `OwnerMapper`, y el integrante B otra sin DTO.
- **Resolución:** se dejó el diseño de B (el servicio devuelve la entidad `Owner`), se borraron `OwnerDTO` y `OwnerMapper`, y se juntaron en `OwnerServiceTest` las pruebas de los tres bloques. Commit `8b1884d`.

### Ejecutar las pruebas

```
./mvnw clean test -Dspring.profiles.active=h2
```
