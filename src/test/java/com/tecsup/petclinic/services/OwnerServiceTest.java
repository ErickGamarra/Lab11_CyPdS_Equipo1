package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.repositories.OwnerRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Slf4j
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @MockitoSpyBean
    private OwnerRepository repository; // Mockito observa las llamadas al repositorio real de H2.

    @Autowired
    private EntityManager entityManager;

    // Integrante A — cada prueba crea su propio dueño y revierte sus cambios al terminar.
    @Test
    @Transactional
    public void testCreateOwner() throws OwnerNotFoundException {
        Owner nuevo = new Owner("Ana", "Rojas", "Calle Uno 10", "Lima", "987654321");
        Owner creado = ownerService.create(nuevo);

        verify(repository).save(nuevo);
        assertNotNull(creado.getId()); // H2 asigna el ID al insertar.
        assertEquals("Ana", creado.getFirstName());
        assertEquals("Rojas", creado.getLastName());
        entityManager.flush();
        entityManager.clear(); // Obliga a consultar la fila guardada en lugar del objeto en memoria.
        Owner guardado = ownerService.findById(creado.getId());
        assertEquals(creado, guardado); // Owner compara sus campos mediante @Data.
    }

    @Test
    @Transactional
    public void testUpdateOwner() throws OwnerNotFoundException {
        Owner original = ownerService.create(new Owner("Ana", "Rojas", "Calle Uno 10", "Lima", "987654321"));
        Integer id = original.getId();
        entityManager.flush();
        entityManager.clear(); // Separa la entidad de H2 para que el cambio requiera llamar a update.
        original.setCity("Cusco");
        original.setTelephone("912345678");

        Owner actualizado = ownerService.update(original);

        assertEquals(id, actualizado.getId());
        entityManager.flush();
        entityManager.clear();
        Owner guardado = ownerService.findById(id);
        assertEquals("Cusco", guardado.getCity());
        assertEquals("912345678", guardado.getTelephone());
        assertEquals("Rojas", guardado.getLastName());
    }

    @Test
    @Transactional
    public void testDeleteOwner() throws OwnerNotFoundException {
        Owner creado = ownerService.create(new Owner("Luis", "Vega", "Calle Dos 20", "Lima", "987654322"));
        Integer id = creado.getId();
        assertTrue(repository.existsById(id)); // Confirma que el dueño existe antes de eliminarlo.

        ownerService.delete(id);

        verify(repository).delete(creado);
        entityManager.flush();
        entityManager.clear();
        assertFalse(repository.existsById(id)); // Comprueba la eliminación en H2 por el mismo ID.
    }

    // Integrante B — Búsquedas y consultas
    // Prueba 1: testFindOwnerById — buscar dueño existente
    @Test
    public void testFindOwnerById() {
        Integer ID = 1;
        String EXPECTED_FIRST_NAME = "George";
        String EXPECTED_LAST_NAME = "Franklin";
        String EXPECTED_CITY = "Madison";

        Owner owner = null;

        try {
            owner = this.ownerService.findById(ID);
        } catch (OwnerNotFoundException e) {
            fail("No debería lanzar excepción para un ID existente: " + e.getMessage());
        }

        log.info("Dueño recuperado de la BD: {}", owner);

        assertNotNull(owner, "El dueño no debe ser nulo");
        assertEquals(ID, owner.getId(), "El ID debe coincidir");
        assertEquals(EXPECTED_FIRST_NAME, owner.getFirstName(), "El nombre debe ser George");
        assertEquals(EXPECTED_LAST_NAME, owner.getLastName(), "El apellido debe ser Franklin");
        assertEquals(EXPECTED_CITY, owner.getCity(), "La ciudad debe ser Madison");
    }

    // Prueba 2: testFindByLastName — buscar por apellido
    @Test
    public void testFindByLastName() {
        String LAST_NAME = "Davis";
        int EXPECTED_SIZE = 2; // En data.sql existen 2 dueños con apellido Davis (id=2 e id=4)

        List<Owner> owners = this.ownerService.findByLastName(LAST_NAME);

        log.info("Dueños encontrados con apellido {}: {}", LAST_NAME, owners);

        assertNotNull(owners, "La lista no debe ser nula");
        assertEquals(EXPECTED_SIZE, owners.size(), "Debe encontrar exactamente 2 dueños con apellido Davis");

        for (Owner owner : owners) {
            assertEquals(LAST_NAME, owner.getLastName(), "El apellido de cada dueño retornado debe ser Davis");
        }
    }

    // Prueba 3: testFindByCity — buscar por ciudad
    @Test
    public void testFindByCity() {
        String CITY = "Monona";
        int EXPECTED_SIZE = 2; // En data.sql existen 2 dueños en Monona: Jean Coleman (id=6) y Jeff Black (id=7)

        List<Owner> owners = this.ownerService.findByCity(CITY);

        log.info("Dueños encontrados en la ciudad {}: {}", CITY, owners);

        assertNotNull(owners, "La lista no debe ser nula");
        assertEquals(EXPECTED_SIZE, owners.size(), "Debe encontrar exactamente 2 dueños en Monona");

        for (Owner owner : owners) {
            assertEquals(CITY, owner.getCity(), "La ciudad de cada dueño retornado debe ser Monona");
        }
    }

    // Integrante C — Casos de error
    // Prueba 1: testFindOwnerById_NotFound — un ID inexistente lanza OwnerNotFoundException
    @Test
    public void testFindOwnerById_NotFound() {
        Integer ID_NOT_EXIST = 666; // No existe en data.sql

        OwnerNotFoundException exception = assertThrows(
                OwnerNotFoundException.class,
                () -> this.ownerService.findById(ID_NOT_EXIST));

        log.info("Excepción esperada: {}", exception.getMessage());

        assertEquals("Owner not found with ID: " + ID_NOT_EXIST, exception.getMessage());
        verify(repository).findById(ID_NOT_EXIST); // Sí consultó la BD
    }

    // Prueba 2: testDeleteOwner_NotFound — borrar un ID inexistente lanza excepción y no borra nada
    @Test
    @Transactional
    public void testDeleteOwner_NotFound() {
        Integer ID_NOT_EXIST = 1000; // No existe en data.sql
        long totalAntes = repository.count();

        OwnerNotFoundException exception = assertThrows(
                OwnerNotFoundException.class,
                () -> this.ownerService.delete(ID_NOT_EXIST));

        log.info("Excepción esperada: {}", exception.getMessage());

        assertEquals("Owner not found with ID: " + ID_NOT_EXIST, exception.getMessage());
        verify(repository, never()).delete(any(Owner.class)); // Nunca llegó a eliminar
        assertEquals(totalAntes, repository.count(), "La cantidad de dueños no debe cambiar");
    }

    // Prueba 3: testFindByLastName_Empty — un apellido sin coincidencias devuelve lista vacía
    @Test
    public void testFindByLastName_Empty() {
        String LAST_NAME_NOT_EXIST = "NoExiste"; // Ningún dueño tiene ese apellido en data.sql

        List<Owner> owners = this.ownerService.findByLastName(LAST_NAME_NOT_EXIST);

        log.info("Dueños encontrados con apellido {}: {}", LAST_NAME_NOT_EXIST, owners.size());

        assertNotNull(owners, "La lista no debe ser nula");
        assertTrue(owners.isEmpty(), "La lista debe estar vacía");
        verify(repository).findByLastName(LAST_NAME_NOT_EXIST);
    }
}
