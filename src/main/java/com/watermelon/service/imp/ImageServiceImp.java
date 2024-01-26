package com.watermelon.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.service.ImageService;
import com.watermelon.service.dto.ImageDTO;
@Service

public class ImageServiceImp implements ImageService{

	@Override
	public List<String> upload(List<MultipartFile> imageFiles) {
		
		return null;
	}

	
}
