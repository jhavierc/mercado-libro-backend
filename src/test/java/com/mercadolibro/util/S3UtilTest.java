package com.mercadolibro.util;

import com.mercadolibro.dto.S3ObjectReqDTO;
import com.mercadolibro.exception.MultipartFileToFileConversionException;
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

import java.io.File;
import java.io.IOException;

import static com.mercadolibro.util.S3Util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3UtilTest {
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private S3Client s3Client;

    @Test
    public void testConvertMultipartFileToFileWithNewFileName() throws Exception {
        String newFileName = "newFileName.txt";
        byte[] content = "Mock content".getBytes();

        when(multipartFile.getBytes()).thenReturn(content);

        S3ObjectReqDTO s3ObjectReqDTO = S3Util.convertMultipartFileToS3ObjectReqDTO(multipartFile, newFileName);

        verify(multipartFile).getBytes();
        assertNotNull(s3ObjectReqDTO);
        assertEquals(newFileName, s3ObjectReqDTO.getName());
    }

    @Test
    public void testConvertMultipartFileToFileWithoutNewFileName() throws Exception {
        String mockedFileName = "mockFile.txt";
        byte[] content = "Mock content".getBytes();
        when(multipartFile.getName()).thenReturn(mockedFileName);
        when(multipartFile.getBytes()).thenReturn(content);

        S3ObjectReqDTO s3ObjectReqDTO = S3Util.convertMultipartFileToS3ObjectReqDTO(multipartFile, null);

        verify(multipartFile).getName();
        verify(multipartFile).getBytes();
        assertNotNull(s3ObjectReqDTO);
        assertEquals(mockedFileName, s3ObjectReqDTO.getName());
    }

    @Test
    public void testConvertMultipartFileToFileException() throws Exception {
        String mockedFileName = "mockFile.txt";
        when(multipartFile.getName()).thenReturn(mockedFileName);
        when(multipartFile.getBytes()).thenThrow(IOException.class);

        MultipartFileToFileConversionException exception = assertThrows(MultipartFileToFileConversionException.class,
                () -> S3Util.convertMultipartFileToS3ObjectReqDTO(multipartFile, null));

        verify(multipartFile).getName();
        assertEquals(MULTIPART_FILE_TO_FILE_CONVERSION_ERROR_FORMAT, exception.getMessage());
    }

    @Test
    public void testGenerateUniqueFileNameSuccess() {
        String originalName = "name";

        String uniqueName = S3Util.generateUniqueFileName(originalName);

        assertTrue(uniqueName.contains("_" + originalName));
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