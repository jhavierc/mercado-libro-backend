package com.mercadolibro.repository.impl;

import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.dto.S3ObjectUploadDTO;
import com.mercadolibro.exception.S3Exception;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.mercadolibro.repository.impl.S3RepositoryImpl.DELETE_FILE_ERROR_FORMAT;
import static com.mercadolibro.repository.impl.S3RepositoryImpl.UPLOAD_FILE_ERROR_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3RepositoryImplTest {
    @Mock
    private S3Client s3Client;
    @InjectMocks
    private S3RepositoryImpl s3Repository;

    @Test
    public void putFileSuccessful() throws Exception{
        // Arrange
        String imagesPath = "testPath/";
        String bucketName = "test-bucket";
        String fileName = "testFile.txt";
        byte[] bytesContent = "Test content".getBytes();

        // Expected
        String expectedKey = imagesPath + fileName;
        S3ObjectUploadDTO expectedReq = new S3ObjectUploadDTO(fileName, new ByteArrayInputStream(bytesContent));
        URL expectedUrl = new URL("https://" + bucketName + ".s3.amazonaws.com/" + expectedKey);
        expectedReq.setKey(expectedKey);

        // Set autowired vars because config is not loaded in tests
        ReflectionTestUtils.setField(s3Repository, "bucketName", bucketName);

        // Mock (but It's not a mock :))
        S3Utilities expectedS3Utilities = S3Utilities.builder().region(Region.US_EAST_1).build();
        PutObjectResponse putObjectResponse = PutObjectResponse.builder().build();

        // Mocks actions
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(putObjectResponse);
        when(s3Client.utilities()).thenReturn(expectedS3Utilities);

        // Act
        S3ObjectDTO result = s3Repository.putFile(expectedReq);

        // Assert
        assertEquals(expectedKey, result.getKey());
        assertEquals(expectedUrl.toString(), result.getUrl());
        assertEquals(bucketName, result.getBucket());
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    public void putFileException() {
        String fileName = "testFile.txt";
        byte[] bytesContent = "Test content".getBytes();

        S3ObjectUploadDTO expectedReq = new S3ObjectUploadDTO(fileName, new ByteArrayInputStream(bytesContent));

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenThrow(new RuntimeException());

        S3Exception exception = assertThrows(S3Exception.class,
                () -> s3Repository.putFile(expectedReq));
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertEquals(UPLOAD_FILE_ERROR_FORMAT, exception.getMessage());
    }

    @Test
    void putFilesSuccess() {
        // Arrange
        String imagesPath = "testPath/";
        String bucketName = "test-bucket";
        byte[] bytesContent1 = "Test content 1".getBytes();
        byte[] bytesContent2 = "Test content 2".getBytes();

        S3ObjectUploadDTO reqDTO1 = new S3ObjectUploadDTO("file1", new ByteArrayInputStream(bytesContent1));
        S3ObjectUploadDTO reqDTO2 = new S3ObjectUploadDTO("file2", new ByteArrayInputStream(bytesContent2));
        reqDTO1.setKey(imagesPath + "file1");
        reqDTO2.setKey(imagesPath + "file2");

        List<S3ObjectUploadDTO> reqDTOs = new ArrayList<>();
        reqDTOs.add(reqDTO1);
        reqDTOs.add(reqDTO2);

        S3ObjectDTO respDTO1 = new S3ObjectDTO("testPath/file1", "https://test-bucket.s3.amazonaws.com/testPath/file1", "test-bucket");
        S3ObjectDTO respDTO2 = new S3ObjectDTO("testPath/file2", "https://test-bucket.s3.amazonaws.com/testPath/file2", "test-bucket");
        List<S3ObjectDTO> expectedResponses = new ArrayList<>();
        expectedResponses.add(respDTO1);
        expectedResponses.add(respDTO2);

        // Set autowired vars because config is not loaded in tests
        ReflectionTestUtils.setField(s3Repository, "bucketName", bucketName);

        // Mock (but It's not a mock :))
        S3Utilities expectedS3Utilities = S3Utilities.builder().region(Region.US_EAST_1).build();
        PutObjectResponse putObjectResponse = PutObjectResponse.builder().build();

        // Mocks actions
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(putObjectResponse);
        when(s3Client.utilities()).thenReturn(expectedS3Utilities);

        // Act
        List<S3ObjectDTO> actualResponses = s3Repository.putFiles(reqDTOs);

        // Assert
        assertEquals(expectedResponses.get(0).getKey(), actualResponses.get(0).getKey());
        assertEquals(expectedResponses.get(0).getUrl(), actualResponses.get(0).getUrl());
        assertEquals(expectedResponses.get(0).getBucket(), actualResponses.get(0).getBucket());

        assertEquals(expectedResponses.get(1).getKey(), actualResponses.get(1).getKey());
        assertEquals(expectedResponses.get(1).getUrl(), actualResponses.get(1).getUrl());
        assertEquals(expectedResponses.get(1).getBucket(), actualResponses.get(1).getBucket());
    }

    @Test
    public void deleteFileSuccess() {
        List<S3ObjectDTO> s3ObjectDTOS = new ArrayList<>();
        s3ObjectDTOS.add(new S3ObjectDTO("test", "test", "test"));
        DeleteObjectsResponse deleteResponse = DeleteObjectsResponse.builder().build();

        when(s3Client.deleteObjects(any(DeleteObjectsRequest.class))).thenReturn(deleteResponse);

        s3Repository.deleteFiles(s3ObjectDTOS);

        verify(s3Client).deleteObjects(any(DeleteObjectsRequest.class));
    }

    @Test
    public void deleteFileException() {
        List<S3ObjectDTO> s3ObjectDTOS = new ArrayList<>();
        s3ObjectDTOS.add(new S3ObjectDTO("test", "test", "test"));

        when(s3Client.deleteObjects(any(DeleteObjectsRequest.class))).thenThrow(new RuntimeException());

        S3Exception exception = assertThrows(S3Exception.class,
                () -> s3Repository.deleteFiles(s3ObjectDTOS));

        verify(s3Client).deleteObjects(any(DeleteObjectsRequest.class));
        assertEquals(DELETE_FILE_ERROR_FORMAT, exception.getMessage());
    }
}
