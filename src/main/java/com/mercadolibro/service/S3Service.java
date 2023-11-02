package com.mercadolibro.service;

import com.mercadolibro.dto.S3ObjectDTO;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    S3ObjectDTO uploadFile(MultipartFile multipartFile);

    void deleteFile(String fileName);
}
