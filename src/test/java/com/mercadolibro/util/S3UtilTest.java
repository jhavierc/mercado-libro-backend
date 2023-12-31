package com.mercadolibro.util;

import com.mercadolibro.dto.S3ObjectUploadDTO;
import com.mercadolibro.exception.MultipartFileToDTOConversionException;
import com.mercadolibro.exception.S3Exception;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

import static com.mercadolibro.util.S3Util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3UtilTest {
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private S3Client s3Client;

    @Test
    public void testConvertMultipartFileToFileWithNewFileName() throws Exception {
        String path = "images/";
        String newFileName = "newFileName.txt";
        String filePath = path + newFileName;
        byte[] content = "Mock content".getBytes();

        when(multipartFile.getBytes()).thenReturn(content);

        S3ObjectUploadDTO s3ObjectUploadDTO = S3Util.convertMultipartFileToS3ObjectReqDTO(multipartFile, newFileName, path);

        verify(multipartFile).getBytes();
        assertNotNull(s3ObjectUploadDTO);
        assertEquals(filePath, s3ObjectUploadDTO.getKey());
    }

    @Test
    public void testConvertMultipartFileToFileWithoutNewFileName() throws Exception {
        String path = "images/";
        String mockedFileName = "mockFile.txt";
        String filePath = path + mockedFileName;
        byte[] content = "Mock content".getBytes();

        when(multipartFile.getName()).thenReturn(mockedFileName);
        when(multipartFile.getBytes()).thenReturn(content);

        S3ObjectUploadDTO s3ObjectUploadDTO = S3Util.convertMultipartFileToS3ObjectReqDTO(multipartFile, null, path);

        verify(multipartFile).getName();
        verify(multipartFile).getBytes();
        assertNotNull(s3ObjectUploadDTO);
        assertEquals(filePath, s3ObjectUploadDTO.getKey());
    }

    @Test
    public void testConvertMultipartFileToFileException() throws Exception {
        String mockedFileName = "mockFile.txt";
        String filePath = "images/";
        when(multipartFile.getName()).thenReturn(mockedFileName);
        when(multipartFile.getBytes()).thenThrow(IOException.class);

        MultipartFileToDTOConversionException exception = assertThrows(MultipartFileToDTOConversionException.class,
                () -> S3Util.convertMultipartFileToS3ObjectReqDTO(multipartFile, null, filePath));

        verify(multipartFile).getName();
        assertEquals(MULTIPART_FILE_TO_DTO_CONVERSION_ERROR_FORMAT, exception.getMessage());
    }

    @Test
    public void testGenerateUniqueFileNameSuccess() {
        String uniqueName = S3Util.generateFileName();

        assertNotNull(uniqueName);
    }

    @Test
    public void testDoesS3FolderExistSuccess() {
        ListObjectsV2Response listObjectsV2Response = ListObjectsV2Response.builder()
                .keyCount(1)
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listObjectsV2Response);

        boolean folderExists = S3Util.doesS3FolderExist(s3Client, "testBucket", "testFolder/");

        verify(s3Client).listObjectsV2(any(ListObjectsV2Request.class));
        assertTrue(folderExists);
    }

    @Test
    public void testDoesS3FolderExistNotFound() {
        ListObjectsV2Response listObjectsV2Response = ListObjectsV2Response.builder()
                .keyCount(0)
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listObjectsV2Response);

        boolean folderExists = S3Util.doesS3FolderExist(s3Client, "testBucket", "testFolder/");

        verify(s3Client).listObjectsV2(any(ListObjectsV2Request.class));
        assertFalse(folderExists);
    }

    @Test
    public void testDoesS3FolderExistException() {
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenThrow(SdkClientException.class);

        S3Exception exception = assertThrows(S3Exception.class,
                () -> S3Util.doesS3FolderExist(s3Client, "testBucket", "testFolder/"));

        verify(s3Client).listObjectsV2(any(ListObjectsV2Request.class));
        assertEquals(S3_FOLDER_EXISTS_ERROR, exception.getMessage());
    }

    @Test
    public void testCreateS3FolderSuccess() {
        PutObjectResponse putObjectResponse = PutObjectResponse.builder()
                .build();

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(putObjectResponse);

        S3Util.createS3Folder(s3Client, "testBucket", "testFolder/");

        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    public void testCreateS3FolderException() {
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenThrow(SdkClientException.class);

        S3Exception exception = assertThrows(S3Exception.class,
                () -> S3Util.createS3Folder(s3Client, "testBucket", "testFolder/"));

        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertEquals(S3_FOLDER_CREATE_ERROR, exception.getMessage());
    }
}