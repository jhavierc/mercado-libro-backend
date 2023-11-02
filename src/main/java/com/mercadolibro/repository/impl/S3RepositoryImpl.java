package com.mercadolibro.repository.impl;

import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.exception.S3Exception;
import com.mercadolibro.repository.S3Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.net.URL;

@Repository
public class S3RepositoryImpl implements S3Repository {
    private final S3Client s3Client;
    private final String bucketName;

    public static final String UPLOAD_FILE_ERROR_FORMAT = "Error while uploading file to S3";
    public static final String DELETE_FILE_ERROR_FORMAT = "Error while deleting file from S3";


    @Autowired
    public S3RepositoryImpl(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public S3ObjectDTO putFile(File file) {
        String fileName = file.getName();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromFile(file));
        } catch (Exception e) {
            throw new S3Exception(UPLOAD_FILE_ERROR_FORMAT);
        }

        URL url = s3Client.utilities().getUrl(r -> r.bucket(bucketName).key(fileName));

        return new S3ObjectDTO(fileName, url.toString(), bucketName);
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(r -> r.bucket(bucketName).key(fileName));
        } catch (Exception e) {
            throw new S3Exception(DELETE_FILE_ERROR_FORMAT);
        }
    }
}
