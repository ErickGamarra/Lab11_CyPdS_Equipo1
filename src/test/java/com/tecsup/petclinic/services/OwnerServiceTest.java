package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mappers.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de OwnerService, bloque C: casos de error.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {

	@Mock
	private OwnerRepository repository; // Simula la base de datos.

	@Spy
	private OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

	@InjectMocks
	private OwnerServiceImpl ownerService; // Servicio real con los colaboradores simulados.

	/**
	 * Buscar un dueño que no existe debe lanzar OwnerNotFoundException.
	 */
	@Test
	public void testFindOwnerById_NotFound() {

		int ID_NOT_EXIST = 999;

		// El repositorio no encuentra ningún dueño con ese ID.
		when(this.repository.findById(ID_NOT_EXIST))
				.thenReturn(Optional.empty());

		OwnerNotFoundException exception = assertThrows(
				OwnerNotFoundException.class,
				() -> this.ownerService.findById(ID_NOT_EXIST));

		log.info("Excepción esperada: {}", exception.getMessage());

		assertEquals("Record not found...!", exception.getMessage());
		verify(this.repository).findById(ID_NOT_EXIST);
		verifyNoMoreInteractions(this.repository);
	}

	/**
	 * Eliminar un dueño que no existe debe lanzar OwnerNotFoundException
	 * y no debe borrar nada.
	 */
	@Test
	public void testDeleteOwner_NotFound() {

		int ID_NOT_EXIST = 999;

		// El repositorio no encuentra ningún dueño con ese ID.
		when(this.repository.findById(ID_NOT_EXIST))
				.thenReturn(Optional.empty());

		OwnerNotFoundException exception = assertThrows(
				OwnerNotFoundException.class,
				() -> this.ownerService.delete(ID_NOT_EXIST));

		log.info("Excepción esperada: {}", exception.getMessage());

		assertEquals("Record not found...!", exception.getMessage());
		verify(this.repository).findById(ID_NOT_EXIST);
		// Nunca debe llegar a eliminar un registro inexistente.
		verify(this.repository, never()).delete(any(Owner.class));
		verifyNoMoreInteractions(this.repository);
	}

	/**
	 * Buscar por un apellido sin coincidencias devuelve una lista vacía
	 * (no null y no lanza excepción).
	 */
	@Test
	public void testFindByLastName_Empty() {

		String LAST_NAME_NOT_EXIST = "NoExiste";

		// El repositorio no encuentra ningún dueño con ese apellido.
		when(this.repository.findByLastName(LAST_NAME_NOT_EXIST))
				.thenReturn(Collections.emptyList());

		List<OwnerDTO> owners = this.ownerService.findByLastName(LAST_NAME_NOT_EXIST);

		log.info("Dueños encontrados: {}", owners.size());

		assertNotNull(owners);
		assertTrue(owners.isEmpty());
		verify(this.repository).findByLastName(LAST_NAME_NOT_EXIST);
		verifyNoMoreInteractions(this.repository);
	}

}
