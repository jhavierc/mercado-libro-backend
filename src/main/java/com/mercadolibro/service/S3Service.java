package com.mercadolibro.service;

import com.mercadolibro.dto.S3ObjectDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    /**
     * Uploads a single MultipartFile to the S3 repository.
     *
     * @param multipartFile The MultipartFile to be uploaded.
     * @return An S3ObjectDTO representing the uploaded file's metadata.
     */
    S3ObjectDTO uploadFile(MultipartFile multipartFile);

    /**
     * Uploads a list of MultipartFiles to the S3 repository.
     *
     * @param multipartFiles The list of MultipartFiles to be uploaded.
     * @return A list of S3ObjectDTOs representing metadata of the uploaded files.
     */
    List<S3ObjectDTO> uploadFiles(List<MultipartFile> multipartFiles);

    /**
     * Replace files to the S3 repository using specified URLs.
     *
     * @param multipartFiles The list of MultipartFiles to be uploaded.
     * @param fileURLs       The corresponding URLs for the files to be uploaded.
     * @return A list of S3ObjectDTOs representing metadata of the uploaded files.
     */
    List<S3ObjectDTO> replaceFilesByURLs(List<MultipartFile> multipartFiles, List<String> fileURLs);

    /**
     * Deletes files from the S3 repository by their object information.
     *
     * @param s3ObjectDTOS A list of S3ObjectDTO representing files to be deleted from the S3 repository.
     */
    void deleteFiles(List<S3ObjectDTO> s3ObjectDTOS);
}
