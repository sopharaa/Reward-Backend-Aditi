package com.phara.pontrix_backend.service.impl;

import com.phara.pontrix_backend.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DigitalOceanSpacesService implements CloudStorageService {

    @Value("${do.spaces.key:}")
    private String accessKey;

    @Value("${do.spaces.secret:}")
    private String secretKey;

    @Value("${do.spaces.endpoint:}")
    private String endpoint;

    @Value("${do.spaces.region:sgp1}")
    private String region;

    @Value("${do.spaces.bucket:}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            S3Client s3Client = createS3Client();

            // Generate unique file name
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
            String fileName = folder + "/" + UUID.randomUUID() + extension;

            // Upload file
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl("public-read")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // Return public URL
            return String.format("https://%s.%s/%s",
                    bucketName,
                    endpoint.replace("https://", ""),
                    fileName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            S3Client s3Client = createS3Client();
            String fileName = getFileNameFromUrl(fileUrl);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to delete file: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getFileNameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "";
        }

        // Extract file name from URL
        // Example: https://bucket.sgp1.digitaloceanspaces.com/rewards/uuid.jpg -> rewards/uuid.jpg
        String[] parts = fileUrl.split(bucketName + "." + endpoint.replace("https://", "") + "/");
        return parts.length > 1 ? parts[1] : "";
    }

    private S3Client createS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(endpoint))
                .forcePathStyle(true)   // VERY IMPORTANT for DigitalOcean
                .build();
    }
}

