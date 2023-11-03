package com.mercadolibro.util;

import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.exception.MultipartFileToFileConversionException;
import com.mercadolibro.exception.S3Exception;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class S3Util {
    public static final String MULTIPART_FILE_TO_FILE_CONVERSION_ERROR_FORMAT = "Error converting MultipartFile to File";
    public static final String S3_FOLDER_EXISTS_ERROR = "Error while checking if folder exists";
    public static final String S3_FOLDER_CREATE_ERROR = "Error while creating folder";


    /**
     * Converts a MultipartFile object to an S3ObjectDTO.
     *
     * @param multipartFile The source file to convert.
     * @param newFileName   The optional new file name. If provided (not null or empty), it will be used as the file name.
     *                     If null or empty, the original name from the MultipartFile will be used.
     * @return The S3ObjectDTO generated from the MultipartFile.
     * @throws MultipartFileToFileConversionException When an error occurs during the conversion.
     */
    public static S3ObjectDTO convertMultipartFileToS3ObjectReqDTO(MultipartFile multipartFile, String newFileName) {
        String fileName = (newFileName != null && !newFileName.isEmpty()) ? newFileName : multipartFile.getName();

        S3ObjectDTO s3ObjectDTO = new S3ObjectDTO();
        s3ObjectDTO.setName(fileName);

        try {
            s3ObjectDTO.setContent(new ByteArrayInputStream(multipartFile.getBytes()));
        }catch (IOException e) {
            throw new MultipartFileToFileConversionException(MULTIPART_FILE_TO_FILE_CONVERSION_ERROR_FORMAT);
        }

        return s3ObjectDTO;
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
     * @throws S3Exception When an error occurs during the folder search request.
     */
    public static boolean doesS3FolderExist(S3Client s3Client, String bucketName, String folderPath) {
        try {
            ListObjectsV2Response response = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(folderPath)
                    .build());

            return response.keyCount() > 0;
        }catch (Exception e){
            throw new S3Exception(S3_FOLDER_EXISTS_ERROR);
        }
    }

    /**
     * Creates a folder within the specified Amazon S3 bucket using the provided S3Client.
     *
     * @param s3Client   The S3Client used to create the folder.
     * @param bucketName The name of the S3 bucket in which the folder will be created.
     * @param folderPath The path of the folder to be created.
     * @throws S3Exception When an error occurs during the folder creation request.
     */
    public static void createS3Folder(S3Client s3Client, String bucketName, String folderPath) {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(folderPath)
                            .build(),
            RequestBody.fromBytes(new byte[0]));
        }
        catch (Exception e){
            throw new S3Exception(S3_FOLDER_CREATE_ERROR);
        }
    }
}
