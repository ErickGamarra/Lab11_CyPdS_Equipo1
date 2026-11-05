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
