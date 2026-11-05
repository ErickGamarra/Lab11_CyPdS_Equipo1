package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.repositories.VetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VetService {
    private final VetRepository repository;

    public Vet create(Vet vet) {
        if (vet.getId() != null) {
            throw new IllegalArgumentException("El veterinario nuevo debe tener el ID vacío");
        }
        return repository.save(vet);
    }

    public Vet findById(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Veterinario inexistente: " + id));
    }

    public Vet update(Integer id, Vet changes) {
        Vet vet = findById(id);
        vet.setFirstName(changes.getFirstName());
        vet.setLastName(changes.getLastName());
        vet.setEmail(changes.getEmail());
        vet.setPhone(changes.getPhone());
        return repository.save(vet);
    }

    public void deactivate(Integer id) {
        Vet vet = findById(id);
        vet.setActive(false); // La baja conserva el registro y sus especialidades.
        repository.save(vet);
    }
}
