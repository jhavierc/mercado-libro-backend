package com.mercadolibro.repository.impl;

import com.mercadolibro.dto.S3ObjectReqDTO;
import com.mercadolibro.dto.S3ObjectRespDTO;
import com.mercadolibro.exception.S3Exception;
import com.mercadolibro.repository.S3Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Repository
public class S3RepositoryImpl implements S3Repository {
    private final S3Client s3Client;
    private final String bucketName;
    private final String imagesPath;

    public static final String UPLOAD_FILE_ERROR_FORMAT = "Error while uploading file to S3";
    public static final String DELETE_FILE_ERROR_FORMAT = "Error while deleting file from S3";

    @Autowired
    public S3RepositoryImpl(S3Client s3Client, String bucketName, String imagesPath) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.imagesPath = imagesPath;
    }

    @Override
    public S3ObjectRespDTO putFile(S3ObjectReqDTO s3ObjectReqDTO) {
        String key = imagesPath + s3ObjectReqDTO.getName();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromInputStream(s3ObjectReqDTO.getContent(), s3ObjectReqDTO.getContent().available()));
        } catch (Exception e) {
            throw new S3Exception(UPLOAD_FILE_ERROR_FORMAT);
        }

        URL url = s3Client.utilities().getUrl(r -> r.bucket(bucketName).key(key));

        return new S3ObjectRespDTO(key, url.toString(), bucketName);
    }

    @Override
    public List<S3ObjectRespDTO> putFiles(List<S3ObjectReqDTO> s3ObjectReqDTOS) {
        List<S3ObjectRespDTO> uploadedObjects = new ArrayList<>();

        for (S3ObjectReqDTO reqDTO : s3ObjectReqDTOS) {
            uploadedObjects.add(putFile(reqDTO));
        }

        return uploadedObjects;
    }

    @Override
    public void deleteFile(String fileName) {
        String key = imagesPath + fileName;

        DeleteObjectRequest dor = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.deleteObject(dor);
        } catch (Exception e) {
            throw new S3Exception(DELETE_FILE_ERROR_FORMAT);
        }
    }
}
