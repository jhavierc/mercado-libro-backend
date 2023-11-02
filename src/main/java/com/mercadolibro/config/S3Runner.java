package com.mercadolibro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

import javax.transaction.Transactional;

import static com.mercadolibro.util.S3Util.createS3Folder;
import static com.mercadolibro.util.S3Util.doesS3FolderExist;

@Configuration
public class S3Runner implements ApplicationRunner {
    /* True to set up S3 */
    @Value("${aws.s3.setUp}")
    boolean setUpS3 = true;

    /* Already injected as @Bean in S3Config, so @Value It's not necessary */
    private final String bucketName;
    private final String imagesPath;

    private final S3Client s3Client;

    @Autowired
    public S3Runner(String bucketName, String imagesPath, S3Client s3Client) {
        this.bucketName = bucketName;
        this.imagesPath = imagesPath;
        this.s3Client = s3Client;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        setUpS3();
    }

    private void setUpS3() {
        if (!setUpS3) return;

        if (!doesS3FolderExist(s3Client, bucketName, imagesPath)) {
            System.out.println("NOT EXISTS");

            createS3Folder(s3Client, bucketName, imagesPath);

            System.out.println("NOW EXISTS");

        }
        System.out.println("FINISHED");

    }
}
