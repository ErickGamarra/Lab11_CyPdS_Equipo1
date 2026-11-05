package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

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
}
