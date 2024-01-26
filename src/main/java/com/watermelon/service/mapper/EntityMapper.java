package com.watermelon.service.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface EntityMapper<D,E> {
	
	D toDTO(E entity);
	default List<D> toDTO(List<E> entities) {
		return Optional.ofNullable(
				entities.stream()
				.map(this::toDTO)
				.collect(Collectors.toList())
				)
				.orElse(Collections.emptyList());
	}
//	E toEntity(D dto);
//	List<E> toEntity(List<D> dtos);

}
