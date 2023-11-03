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

    /**
     * Converts a MultipartFile object to a File.
     *
     * @param multipartFile The source file to convert.
     * @param newFileName   The optional new file name. If provided (not null or empty), it will be used as the file name.
     *                     If null or empty, the original name from the MultipartFile will be used.
     * @return The File generated from the MultipartFile.
     * @throws MultipartFileToFileConversionException When an error occurs during the conversion.
     */
    public static File convertMultipartFileToFile(MultipartFile multipartFile, String newFileName) {
        String fileName = (newFileName != null && !newFileName.isEmpty()) ? newFileName : multipartFile.getName();

        File file = new File(fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        }catch (IOException e) {
            throw new MultipartFileToFileConversionException(MULTIPART_FILE_TO_FILE_CONVERSION_ERROR_FORMAT);
        }

        return file;
    }

    /**
     * Generates a unique file name by appending the current system time in milliseconds to the original file name.
     *
     * @param originalFileName The original file name to which a unique identifier will be appended.
     * @return The generated unique file name combining the current system time and the original file name.
     */
    public static String generateUniqueFileName(String originalFileName) {
        return System.currentTimeMillis() + "_" + originalFileName;
    }

    /**
     * Checks if a specified folder exists in the given Amazon S3 bucket using the provided S3Client.
     *
     * @param s3Client   The S3Client used to check the existence of the folder.
     * @param bucketName The name of the S3 bucket to search within.
     * @param folderPath The path of the folder to check for existence.
     * @return True if the specified folder exists in the S3 bucket; otherwise, false.
     */
    public static boolean doesS3FolderExist(S3Client s3Client, String bucketName, String folderPath) {
        ListObjectsV2Response response = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folderPath)
                .build());

        return response.keyCount() > 0;
    }

    /**
     * Creates a folder within the specified Amazon S3 bucket using the provided S3Client.
     *
     * @param s3Client   The S3Client used to create the folder.
     * @param bucketName The name of the S3 bucket in which the folder will be created.
     * @param folderPath The path of the folder to be created.
     */
    public static void createS3Folder(S3Client s3Client, String bucketName, String folderPath) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(folderPath)
                        .build(),
                RequestBody.fromBytes(new byte[0]));
    }
}
