package com.mercadolibro.service.impl;

import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.dto.S3ObjectUploadDTO;
import com.mercadolibro.repository.S3Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3ServiceImplTest {
    @Mock
    private S3Repository s3Repository;

    @InjectMocks
    private S3ServiceImpl s3Service;

    @Test
    void uploadFileSuccess() {
        String fileName = "testFile.txt";
        String url = "https://url";
        String bucketName = "my-bucket-name";

        MockMultipartFile file = new MockMultipartFile("testFile", "testFile.txt", "text/plain", "content".getBytes());

        when(s3Repository.putFile(any(S3ObjectUploadDTO.class)))
                .thenReturn(new S3ObjectDTO(fileName, url, bucketName));

        S3ObjectDTO result = s3Service.uploadFile(file);

        assertEquals(fileName, result.getKey());
        assertEquals(url, result.getUrl());
        assertEquals(bucketName, result.getBucket());
        verify(s3Repository).putFile(any(S3ObjectUploadDTO.class));
    }

    @Test
    void uploadFiles_Success() {
        MockMultipartFile file1 = new MockMultipartFile("file1", "file1.txt", "text/plain", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "file2.txt", "text/plain", "content2".getBytes());
        List<MultipartFile> files = Arrays.asList(file1, file2);

        when(s3Repository.putFile(any(S3ObjectUploadDTO.class)))
                .thenReturn(new S3ObjectDTO("file1.txt", "https://url1", "my-bucket-name"))
                .thenReturn(new S3ObjectDTO("file2.txt", "https://url2", "my-bucket-name"));

        List<S3ObjectDTO> result = s3Service.uploadFiles(files);

        assertEquals(2, result.size());
        assertEquals("file1.txt", result.get(0).getKey());
        assertEquals("https://url1", result.get(0).getUrl());
        assertEquals("my-bucket-name", result.get(0).getBucket());

        assertEquals("file2.txt", result.get(1).getKey());
        assertEquals("https://url2", result.get(1).getUrl());
        assertEquals("my-bucket-name", result.get(1).getBucket());

        verify(s3Repository, times(2)).putFile(any(S3ObjectUploadDTO.class));
    }

    @Test
    void deleteFile_Success() {
        List<S3ObjectDTO> s3ObjectDTOS = new ArrayList<>();
        s3ObjectDTOS.add(new S3ObjectDTO("test", "test", "test"));

        s3Service.deleteFiles(s3ObjectDTOS);

        verify(s3Repository).deleteFiles(s3ObjectDTOS);
    }
}
