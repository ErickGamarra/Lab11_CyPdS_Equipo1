package com.tecsup.petclinic.exceptions;

/**
 * Se lanza cuando no existe el dueño solicitado.
 */
public class OwnerNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public OwnerNotFoundException(String message) {
		super(message);
	}

}
