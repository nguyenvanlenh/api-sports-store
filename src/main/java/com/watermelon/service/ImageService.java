package com.watermelon.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.watermelon.service.dto.ImageDTO;

public interface ImageService {

	List<String> upload(List<MultipartFile> imageFiles);
}
