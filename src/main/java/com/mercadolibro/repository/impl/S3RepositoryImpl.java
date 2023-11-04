package com.mercadolibro.repository.impl;

import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.dto.S3ObjectUploadDTO;
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
    public S3ObjectDTO putFile(S3ObjectUploadDTO s3ObjectUploadDTO) {
        String key = imagesPath + s3ObjectUploadDTO.getName();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("image/png") // Amazon forces all images to be png type in metadata to be previewed through the browser with the URL. See more: https://repost.aws/knowledge-center/cloudfront-troubleshooting-images
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(s3ObjectUploadDTO.getContent(), s3ObjectUploadDTO.getContent().available()));
        } catch (Exception e) {
            throw new S3Exception(UPLOAD_FILE_ERROR_FORMAT);
        }

        URL url = s3Client.utilities().getUrl(r -> r.bucket(bucketName).key(key));

        return new S3ObjectDTO(key, url.toString(), bucketName);
    }

    @Override
    public List<S3ObjectDTO> putFiles(List<S3ObjectUploadDTO> s3ObjectUploadDTOS) {
        List<S3ObjectDTO> uploadedObjects = new ArrayList<>();

        for (S3ObjectUploadDTO reqDTO : s3ObjectUploadDTOS) {
            uploadedObjects.add(putFile(reqDTO));
        }

        return uploadedObjects;
    }

    @Override
    public void deleteFiles(List<S3ObjectDTO> s3ObjectDTOS) {
        ArrayList<ObjectIdentifier> toDelete = new ArrayList<>();
        for (S3ObjectDTO s3ObjectDTO : s3ObjectDTOS) {
            toDelete.add(ObjectIdentifier.builder()
                    .key(s3ObjectDTO.getKey())
                    .build());
        }

        try {
            DeleteObjectsRequest dor = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder()
                            .objects(toDelete).build())
                    .build();

            s3Client.deleteObjects(dor);
        } catch (Exception e) {
            throw new S3Exception(DELETE_FILE_ERROR_FORMAT);
        }
    }
}
