package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;

import java.util.List;

public interface OwnerService {

	/**
	 * Busca un dueño por su ID.
	 *
	 * @throws OwnerNotFoundException si no existe
	 */
	OwnerDTO findById(Integer id) throws OwnerNotFoundException;

	/**
	 * Elimina un dueño por su ID.
	 *
	 * @throws OwnerNotFoundException si no existe
	 */
	void delete(Integer id) throws OwnerNotFoundException;

	/**
	 * Busca los dueños con ese apellido; lista vacía si no hay coincidencias.
	 */
	List<OwnerDTO> findByLastName(String lastName);
}
