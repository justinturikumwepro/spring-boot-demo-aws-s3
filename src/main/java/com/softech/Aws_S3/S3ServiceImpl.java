package com.softech.Aws_S3;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softech.Aws_S3.config.AWSConfig;
import com.softech.Aws_S3.service.S3Service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service("SDK")
@Slf4j
public class S3ServiceImpl implements S3Service {
    @Autowired
    private AWSConfig awsConfig;

    private static final String BUCKET_NAME = "excelfiles-justin";

    @Override
    public void uploadFile(String keyName, File fileToUpload) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(BUCKET_NAME).key(keyName)
                .build();
        awsConfig.s3Client().putObject(putObjectRequest, RequestBody.fromFile(fileToUpload));
    }

    @Override
    public byte[] downloadFile(String downloadFileName) throws IOException {
        log.info("Downloading file {} from S3 bucket: {}", BUCKET_NAME, downloadFileName);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(BUCKET_NAME).key(downloadFileName)
                .build();
        if (getObjectRequest == null) {
            log.error("File {} is not found}", downloadFileName);
            return null;
        }
        return awsConfig.s3Client().getObjectAsBytes(getObjectRequest).asByteArray();
    }

    @Override
    public List<String> getFilesInBucket() {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(BUCKET_NAME).build();
        return awsConfig.s3Client().listObjectsV2(listObjectsV2Request).contents().stream()
                .map(s3Object -> s3Object.key())
                .toList();
    }

}
