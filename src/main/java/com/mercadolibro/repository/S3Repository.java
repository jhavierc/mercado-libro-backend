package com.mercadolibro.repository;

import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.dto.S3ObjectRespDTO;

import java.util.List;

public interface S3Repository {
    /**
     * Uploads a single file to the S3 bucket.
     *
     * @param s3ObjectDTO The object containing the file details to be uploaded.
     * @return An object containing the details of the uploaded file in the S3 bucket.
     */
    S3ObjectRespDTO putFile(S3ObjectDTO s3ObjectDTO);

    /**
     * Uploads multiple files to the S3 bucket.
     *
     * @param s3ObjectDTOS The list of objects containing the details of files to be uploaded.
     * @return A list of objects containing the details of the uploaded files in the S3 bucket.
     */
    List<S3ObjectRespDTO> putFiles(List<S3ObjectDTO> s3ObjectDTOS);

    /**
     * Deletes a file from the S3 bucket.
     *
     * @param fileName The name of the file to be deleted.
     */
    void deleteFile(String fileName);
}
