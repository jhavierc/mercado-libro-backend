package com.mercadolibro.util;

import com.mercadolibro.dto.S3ObjectUploadDTO;
import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.exception.MultipartFileToDTOConversionException;
import com.mercadolibro.exception.S3Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class S3Util {
    public static final String MULTIPART_FILE_TO_DTO_CONVERSION_ERROR_FORMAT = "Error converting MultipartFile to S3ObjectUploadDTO";
    public static final String S3_FOLDER_EXISTS_ERROR = "Error while checking if folder exists";
    public static final String S3_FOLDER_CREATE_ERROR = "Error while creating folder";
    public static final String S3_URL_DOES_NOT_MATCH_PATTERN_ERROR = "Error: S3 URL does not match the expected pattern. URL example: https://my-bucket-name.s3.amazonaws.com/images/example-file.jpg";

    /**
     * Converts a MultipartFile object to an S3ObjectUploadDTO.
     *
     * @param multipartFile The source file to convert.
     * @param newFileName   The optional new file name. If provided (not null or empty), it will be used as the file name.
     *                     If null or empty, the original name from the MultipartFile will be used.
     * @param filePath      The path in the S3 bucket where the file will be stored.
     * @return The S3ObjectUploadDTO generated from the MultipartFile.
     * @throws MultipartFileToDTOConversionException When an error occurs during the conversion.
     */
    public static S3ObjectUploadDTO convertMultipartFileToS3ObjectReqDTO(MultipartFile multipartFile, String newFileName, String filePath) {
        String fileName = (newFileName != null && !newFileName.isEmpty()) ? newFileName : multipartFile.getName();

        S3ObjectUploadDTO s3ObjectUploadDTO = new S3ObjectUploadDTO();
        s3ObjectUploadDTO.setKey(filePath + fileName);

        try {
            s3ObjectUploadDTO.setContent(new ByteArrayInputStream(multipartFile.getBytes()));
        } catch (IOException e) {
            throw new MultipartFileToDTOConversionException(MULTIPART_FILE_TO_DTO_CONVERSION_ERROR_FORMAT);
        }

        return s3ObjectUploadDTO;
    }
    /**
     * Generates a unique file name by combining the current system time in milliseconds
     * with the sanitized version of the original file name, including a unique identifier.
     *
     * @return The generated unique file name combining the formatted current system time and a UUID.
     */
    public static String generateFileName() {
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedDate = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String uniqueID = UUID.randomUUID().toString();

        return formattedDate + "-" + uniqueID;
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

    /**
     * Parses an S3 URL and extracts the bucket and key parts.
     *
     * @param s3Url The S3 URL to be parsed.
     * @return An S3ObjectDTO containing the bucket, key, and the URL parsed from the S3 URL.
     * @throws S3Exception When the provided URL does not match the expected S3 URL pattern.
     */
    public static S3ObjectDTO parseS3Url(String s3Url) {
        try {
            URL url = new URL(s3Url);
            String host = url.getHost();

            if (host.endsWith(".s3.amazonaws.com")) {
                String bucket = host.replace(".s3.amazonaws.com", "");
                String key = url.getPath().substring(1);

                S3ObjectDTO s3Object = new S3ObjectDTO();
                s3Object.setBucket(bucket);
                s3Object.setKey(key);
                s3Object.setUrl(s3Url);

                return s3Object;
            }else {
                throw new S3Exception(S3_URL_DOES_NOT_MATCH_PATTERN_ERROR, HttpStatus.BAD_REQUEST.value());
            }
        } catch (Exception e) {
            throw new S3Exception(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
