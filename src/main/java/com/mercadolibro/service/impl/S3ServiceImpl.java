package com.mercadolibro.service.impl;

import com.mercadolibro.dto.S3ObjectUploadDTO;
import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.exception.S3Exception;
import com.mercadolibro.repository.S3Repository;
import com.mercadolibro.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.mercadolibro.util.S3Util.convertMultipartFileToS3ObjectReqDTO;
import static com.mercadolibro.util.S3Util.generateFileName;

@Service
public class S3ServiceImpl implements S3Service {
    private final S3Repository s3Repository;
    private final String imagesPath;
    public static final String INVALID_URLS_COUNT_ERROR = "The number of image URLs to replace does not match the number of new images.";

    @Autowired
    public S3ServiceImpl(S3Repository s3Repository, String imagesPath) {
        this.s3Repository = s3Repository;
        this.imagesPath = imagesPath;
    }

    @Override
    public S3ObjectDTO uploadFile(MultipartFile multipartFile) {
        String uniqueFileName = generateFileName();
        S3ObjectUploadDTO s3ObjectUploadDTO = convertMultipartFileToS3ObjectReqDTO(multipartFile, uniqueFileName, imagesPath);

        return s3Repository.putFile(s3ObjectUploadDTO);
    }

    @Override
    public List<S3ObjectDTO> uploadFiles(List<MultipartFile> multipartFiles) {
        List<S3ObjectUploadDTO> s3ObjectUploadDTOS = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String uniqueFileName = generateFileName();
            S3ObjectUploadDTO s3ObjectUploadDTO = convertMultipartFileToS3ObjectReqDTO(file, uniqueFileName, imagesPath);

            s3ObjectUploadDTOS.add(s3ObjectUploadDTO);
        }

        return s3Repository.putFiles(s3ObjectUploadDTOS);
    }

    @Override
    public List<S3ObjectDTO> replaceFilesByURLs(List<MultipartFile> multipartFiles, List<String> fileURLs) { // TODO: improve image replacement order handling, actually we are trusting the frontend to send the correct order
        if (multipartFiles.size() != fileURLs.size()) {
            throw new S3Exception(INVALID_URLS_COUNT_ERROR, HttpStatus.BAD_REQUEST.value());
        }

        List<S3ObjectDTO> uploadedObjects = new ArrayList<>();

        for (int i = 0; i < multipartFiles.size(); i++) {
            MultipartFile file = multipartFiles.get(i);

            String url = fileURLs.get(i);
            String[] urlParts = url.split("/");
            String name = urlParts[urlParts.length - 1];

            S3ObjectUploadDTO s3ObjectUploadDTO = convertMultipartFileToS3ObjectReqDTO(file, name, imagesPath);

            S3ObjectDTO uploadedObject = s3Repository.putFile(s3ObjectUploadDTO);
            uploadedObjects.add(uploadedObject);
        }

        return uploadedObjects;
    }

    @Override
    public void deleteFiles(List<S3ObjectDTO> s3ObjectDTOS) {
        s3Repository.deleteFiles(s3ObjectDTOS);
    }
}
