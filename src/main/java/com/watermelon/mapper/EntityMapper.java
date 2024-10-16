package com.watermelon.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface EntityMapper<D, E> {

	D toDTO(E entity);

	default List<D> toDTO(List<E> entities) {
		return Optional.ofNullable(entities)
				.map(list -> list.stream().map(this::toDTO).toList())
				.orElse(Collections.emptyList());

	}
}
