package com.mercadolibro.util;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3Util {
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
