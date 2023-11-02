package com.mercadolibro.util;

import com.mercadolibro.exception.MultipartFileToFileConversionException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class S3Util {
    public static final String MULTIPART_FILE_TO_FILE_CONVERSION_ERROR_FORMAT = "Error converting MultipartFile to File";

    public static File convertMultipartFileToFile(MultipartFile multipartFile, String newFileName) {
        String fileName = (newFileName != null && !newFileName.isEmpty()) ? newFileName : multipartFile.getOriginalFilename();

        File file = new File(fileName); // TODO: Check not null filename in book controller

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        }catch (IOException e) {
            throw new MultipartFileToFileConversionException(MULTIPART_FILE_TO_FILE_CONVERSION_ERROR_FORMAT);
        }

        return file;
    }

    public static String generateUniqueFileName(String originalFileName) {
        return System.currentTimeMillis() + "_" + originalFileName;
    }

    public  static boolean doesS3FolderExist(S3Client s3Client, String bucketName, String folderPath) {
        ListObjectsV2Response response = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folderPath)
                .build());

        return response.keyCount() > 0;
    }

    public  static void createS3Folder(S3Client s3Client, String bucketName, String folderPath) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(folderPath)
                        .build(),
                RequestBody.fromBytes(new byte[0]));
    }
}
