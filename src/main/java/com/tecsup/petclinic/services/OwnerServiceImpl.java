package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mappers.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

	private final OwnerRepository ownerRepository;
	private final OwnerMapper ownerMapper;

	@Override
	public OwnerDTO findById(Integer id) throws OwnerNotFoundException {

		Optional<Owner> owner = ownerRepository.findById(id);

		if (!owner.isPresent())
			throw new OwnerNotFoundException("Record not found...!");

		return this.ownerMapper.mapToDto(owner.get());
	}

	@Override
	public void delete(Integer id) throws OwnerNotFoundException {

		OwnerDTO owner = findById(id);

		ownerRepository.delete(this.ownerMapper.mapToEntity(owner));
	}

	@Override
	public List<OwnerDTO> findByLastName(String lastName) {

		List<Owner> owners = ownerRepository.findByLastName(lastName);

		owners.forEach(owner -> log.info("{}", owner));

		return owners
				.stream()
				.map(this.ownerMapper::mapToDto)
				.collect(Collectors.toList());
	}
}
