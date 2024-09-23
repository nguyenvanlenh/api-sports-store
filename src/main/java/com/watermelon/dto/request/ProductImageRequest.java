package com.watermelon.dto.request;

import lombok.Builder;

@Builder
public record ProductImageRequest(Long id,String path){
}
