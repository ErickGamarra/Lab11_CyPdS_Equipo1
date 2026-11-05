package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
