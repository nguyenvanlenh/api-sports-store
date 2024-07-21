package com.watermelon.service.imp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.watermelon.service.ImageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class CloudinaryServiceImp implements ImageService {

	private final Cloudinary cloudinary;

	@Transactional
	@Override
	public List<String> upload(List<MultipartFile> imageFiles) {
		

		return imageFiles.stream().map(file -> {
			try {
				Map<String, String> result = cloudinary.uploader()
						.upload(file.getBytes(), ObjectUtils.emptyMap());
				return result.get("url");
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}).toList();
	}

}
