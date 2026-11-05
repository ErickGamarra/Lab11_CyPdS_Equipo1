package com.tecsup.petclinic.services;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

}
