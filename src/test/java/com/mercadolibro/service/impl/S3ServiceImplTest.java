package com.mercadolibro.service.impl;

import com.mercadolibro.dto.S3ObjectReqDTO;
import com.mercadolibro.dto.S3ObjectRespDTO;
import com.mercadolibro.repository.S3Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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
        String bucketName = "bucket";

        MockMultipartFile file = new MockMultipartFile("testFile", "testFile.txt", "text/plain", "content".getBytes());

        when(s3Repository.putFile(any(S3ObjectReqDTO.class)))
                .thenReturn(new S3ObjectRespDTO(fileName, url, bucketName));

        S3ObjectRespDTO result = s3Service.uploadFile(file);

        assertEquals(fileName, result.getPath());
        assertEquals(url, result.getUrl());
        assertEquals(bucketName, result.getBucket());
        verify(s3Repository).putFile(any(S3ObjectReqDTO.class));
    }

    @Test
    void uploadFiles_Success() {
        MockMultipartFile file1 = new MockMultipartFile("file1", "file1.txt", "text/plain", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "file2.txt", "text/plain", "content2".getBytes());
        List<MultipartFile> files = Arrays.asList(file1, file2);

        when(s3Repository.putFile(any(S3ObjectReqDTO.class)))
                .thenReturn(new S3ObjectRespDTO("file1.txt", "https://url1", "bucket1"))
                .thenReturn(new S3ObjectRespDTO("file2.txt", "https://url2", "bucket2"));

        List<S3ObjectRespDTO> result = s3Service.uploadFiles(files);

        assertEquals(2, result.size());
        assertEquals("file1.txt", result.get(0).getPath());
        assertEquals("https://url1", result.get(0).getUrl());
        assertEquals("bucket1", result.get(0).getBucket());

        assertEquals("file2.txt", result.get(1).getPath());
        assertEquals("https://url2", result.get(1).getUrl());
        assertEquals("bucket2", result.get(1).getBucket());

        verify(s3Repository, times(2)).putFile(any(S3ObjectReqDTO.class));
    }

    @Test
    void deleteFile_Success() {
        String fileName = "testFile.txt";
        s3Service.deleteFile(fileName);
        verify(s3Repository).deleteFile(fileName);
    }
}
