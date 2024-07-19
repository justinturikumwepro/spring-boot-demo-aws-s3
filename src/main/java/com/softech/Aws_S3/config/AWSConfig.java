package com.softech.Aws_S3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {

    // If there are existing profiles in the /users/{user}/.aws/credentials ; and
    // want to use default by default otherwise pass profile name
    ProfileCredentialsProvider profileCredentialsProvider = ProfileCredentialsProvider.create("developer-justin");

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().region(Region.CA_CENTRAL_1)
                .credentialsProvider(profileCredentialsProvider)
                .build();
    }

}
