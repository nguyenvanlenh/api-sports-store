package com.watermelon.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface EntityMapper<D,E> {
	
	D toDTO(E entity);
	default List<D> toDTO(List<E> entities) {
		return Optional.ofNullable(
				entities).orElse(Collections.emptyList()).stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}
}
