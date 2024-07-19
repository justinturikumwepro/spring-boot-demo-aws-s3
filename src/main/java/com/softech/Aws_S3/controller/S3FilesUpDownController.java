package com.softech.Aws_S3.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.softech.Aws_S3.service.S3Service;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/s3")
@Slf4j
public class S3FilesUpDownController {

    @Autowired
    private S3Service s3Service;

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("file_name") String fileName) {
        log.info(">>>Downloading file from S3 bucket: {}", fileName);
        try {
            byte[] content = s3Service.downloadFile(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .body(content);
        } catch (Exception e) {
            log.error("Downloading Error occurred: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file_name") MultipartFile multipartFile) {
        log.info(">>>Uploading file to S3 bucket: {}", multipartFile.getOriginalFilename());
        try {
            File file = convertMultipartFileToFile(multipartFile);
            multipartFile.transferTo(file);
            s3Service.uploadFile(multipartFile.getOriginalFilename(), file);
            return new ResponseEntity<>("File uploaded successfully: " + multipartFile.getOriginalFilename(),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.error("Uploading Error occurred: {}", e.getMessage());
            return new ResponseEntity<>("Error uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buckets")
    private List<String> listBuckets() {
        log.info(">>>Listing files in S3 bucket");
        return s3Service.getFilesInBucket();
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Create a temporary file
        File file = File.createTempFile("temp-", multipartFile.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
        return file;
    }

}
