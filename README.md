# Spring Boot : Application PetClinic

## Features  

### 1.  Unit Test  --> Tag v1.0.0

mvn test -Dspring.profiles.active=h2

## Caso 1 · Pruebas unitarias de veterinarios

**Integrante A:** Jose Contreras Cabrera (identidad configurada en Git).
Rama: `feature/pruebas-crud-veterinarios`.

| Prueba de VetServiceTest | Qué comprueba |
| --- | --- |
| `testCreateVet` | Guarda el veterinario y devuelve el ID asignado. |
| `testUpdateVet` | Actualiza nombre, apellido, correo y teléfono, y conserva el ID y el estado. |
| `testFindVetById` | Recupera el veterinario correspondiente al ID solicitado. |
| `testDeactivateVet` | Guarda el estado inactivo y conserva el registro. Incluida para el requisito de eliminación del caso 1. |

Las cuatro pruebas usan JUnit 5 y Mockito. Para ejecutar solo este bloque en Windows:

```powershell
.\mvnw.cmd -Dtest=VetServiceTest test
```

Para verificar todo el proyecto, ejecutar `.\mvnw.cmd test` en Windows o `./mvnw test` en Linux/macOS.

**Coordinación del equipo:** la revisión, la integración de las ramas y el registro de los conflictos reales se completan en el repositorio compartido. Hasta este commit no se han producido conflictos en esta rama.
