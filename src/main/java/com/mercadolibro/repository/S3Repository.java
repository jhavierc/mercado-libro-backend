package com.mercadolibro.repository;

import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.dto.S3ObjectUploadDTO;

import java.util.List;

public interface S3Repository {
    /**
     * Uploads a single file to the S3 bucket.
     *
     * @param s3ObjectUploadDTO The object containing the file details to be uploaded.
     * @return An object containing the details of the uploaded file in the S3 bucket.
     */
    S3ObjectDTO putFile(S3ObjectUploadDTO s3ObjectUploadDTO);

    /**
     * Uploads multiple files to the S3 bucket.
     *
     * @param s3ObjectUploadDTOS The list of objects containing the details of files to be uploaded.
     * @return A list of objects containing the details of the uploaded files in the S3 bucket.
     */
    List<S3ObjectDTO> putFiles(List<S3ObjectUploadDTO> s3ObjectUploadDTOS);

    /**
     * Deletes a list of files from the S3 repository by their object information.
     *
     * @param s3ObjectDTOS A list of S3ObjectDTO representing files to be deleted from the S3 repository.
     */
    void deleteFiles(List<S3ObjectDTO> s3ObjectDTOS);
}
