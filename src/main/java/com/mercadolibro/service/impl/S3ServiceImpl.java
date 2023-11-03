package com.mercadolibro.service.impl;

import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.dto.S3ObjectRespDTO;
import com.mercadolibro.repository.S3Repository;
import com.mercadolibro.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.mercadolibro.util.S3Util.convertMultipartFileToS3ObjectReqDTO;
import static com.mercadolibro.util.S3Util.generateUniqueFileName;

@Service
public class S3ServiceImpl implements S3Service {
    private final S3Repository s3Repository;

    @Autowired
    public S3ServiceImpl(S3Repository s3Repository) {
        this.s3Repository = s3Repository;
    }

    @Override
    public S3ObjectRespDTO uploadFile(MultipartFile multipartFile) {
        String uniqueFileName = generateUniqueFileName(multipartFile.getName());
        S3ObjectDTO s3ObjectDTO = convertMultipartFileToS3ObjectReqDTO(multipartFile, uniqueFileName);

        return s3Repository.putFile(s3ObjectDTO);
    }

    @Override
    public List<S3ObjectRespDTO> uploadFiles(List<MultipartFile> multipartFiles) {
        List<S3ObjectRespDTO> uploadedObjects = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            uploadedObjects.add(uploadFile(file));
        }

        return uploadedObjects;
    }

    @Override
    public void deleteFile(String fileName) {
        s3Repository.deleteFile(fileName);
    }
}
