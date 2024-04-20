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

@Service
@Primary
public class CloudinaryServiceImp implements ImageService {

	@Value("${cloudinary.cloud-name}")
	private String cloudName;

	@Value("${cloudinary.api-key}")
	private String apiKey;

	@Value("${cloudinary.api-secret}")
	private String apiSecret;

	@Transactional
	@Override
	public List<String> upload(List<MultipartFile> imageFiles) {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret));

		return imageFiles.stream().map(file -> {
			try {
				Map<String, String> result = cloudinary.uploader()
						.upload(file.getBytes(), ObjectUtils.emptyMap());
				return result.get("url").toString();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}).toList();
	}

}
