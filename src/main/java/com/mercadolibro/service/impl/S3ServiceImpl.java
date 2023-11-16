package com.mercadolibro.service.impl;

import com.mercadolibro.dto.S3ObjectUploadDTO;
import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.repository.S3Repository;
import com.mercadolibro.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void deleteFiles(List<S3ObjectDTO> s3ObjectDTOS) {
        s3Repository.deleteFiles(s3ObjectDTOS);
    }
}
