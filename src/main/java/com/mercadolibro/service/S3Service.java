package com.mercadolibro.service;

import com.mercadolibro.dto.S3ObjectDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    S3ObjectDTO uploadFile(MultipartFile multipartFile);

    List<S3ObjectDTO> uploadFiles(List<MultipartFile> multipartFiles);

    void deleteFile(String fileName);
}
