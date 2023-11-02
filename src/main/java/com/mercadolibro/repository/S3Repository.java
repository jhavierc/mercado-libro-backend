package com.mercadolibro.repository;

import com.mercadolibro.dto.S3ObjectDTO;

import java.io.File;

public interface S3Repository {
    S3ObjectDTO putFile(File file);

    void deleteFile(String fileName);
}
