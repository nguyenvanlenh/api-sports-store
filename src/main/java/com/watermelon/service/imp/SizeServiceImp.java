package com.watermelon.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watermelon.model.entity.Size;
import com.watermelon.repository.SizeRepository;
import com.watermelon.service.SizeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizeServiceImp implements SizeService{
	
	SizeRepository sizeRepository;

	@Override
	public List<Size> getAllSizes() {
		return sizeRepository.findAll();
	}

	
}
