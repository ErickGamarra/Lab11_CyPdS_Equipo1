package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.repositories.VetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetServiceTest {
    @Mock
    private VetRepository repository; // Simula la persistencia para aislar el servicio.
    @InjectMocks
    private VetService service; // Mockito inyecta el repositorio en el servicio real.

    @Test
    void testCreateVet() {
        Vet nuevo = veterinario(null, "Ana", "Rojas");
        Vet guardado = veterinario(7, "Ana", "Rojas");
        when(repository.save(nuevo)).thenReturn(guardado); // Simula el ID generado.

        Vet resultado = service.create(nuevo);

        assertEquals(7, resultado.getId());
        assertEquals("Ana", resultado.getFirstName());
        assertEquals("Rojas", resultado.getLastName());
        assertTrue(resultado.isActive());
        verify(repository).save(nuevo);
    }

    @Test
    void testUpdateVet() {
        Vet existente = veterinario(7, "Ana", "Rojas");
        existente.setActive(false);
        Vet cambios = veterinario(null, "Andrea", "Luna");
        cambios.setEmail("andrea@example.org");
        cambios.setPhone("999123456");
        when(repository.findById(7)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Vet resultado = service.update(7, cambios);

        assertEquals(7, resultado.getId()); // La edición conserva la identidad.
        assertEquals("Andrea", resultado.getFirstName());
        assertEquals("Luna", resultado.getLastName());
        assertEquals("andrea@example.org", resultado.getEmail());
        assertEquals("999123456", resultado.getPhone());
        assertFalse(resultado.isActive()); // Actualizar sus datos no lo reactiva.
        verify(repository).save(existente);
    }

    @Test
    void testFindVetById() {
        Vet esperado = veterinario(7, "Ana", "Rojas");
        when(repository.findById(7)).thenReturn(Optional.of(esperado));

        Vet resultado = service.findById(7);

        assertSame(esperado, resultado); // Devuelve el registro encontrado para ese ID.
        verify(repository).findById(7);
        verifyNoMoreInteractions(repository); // Una consulta no debe escribir datos.
    }

    @Test
    void testDeactivateVet() {
        Vet existente = veterinario(7, "Ana", "Rojas");
        when(repository.findById(7)).thenReturn(Optional.of(existente));

        service.deactivate(7);

        assertFalse(existente.isActive()); // El registro sigue existiendo, ahora inactivo.
        verify(repository).findById(7);
        verify(repository).save(existente);
        verifyNoMoreInteractions(repository); // Detecta un borrado físico adicional.
    }

    private Vet veterinario(Integer id, String nombre, String apellido) {
        Vet vet = new Vet();
        vet.setId(id);
        vet.setFirstName(nombre);
        vet.setLastName(apellido);
        return vet;
    }
}
