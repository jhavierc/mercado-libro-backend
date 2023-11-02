package com.mercadolibro.repository;

import com.mercadolibro.dto.S3ObjectDTO;

import java.io.File;
import java.util.List;

public interface S3Repository {
    S3ObjectDTO putFile(File file);

    List<S3ObjectDTO> putFiles(List<File> file);

    void deleteFile(String fileName);
}
