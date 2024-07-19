package com.softech.Aws_S3.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface S3Service {
    void uploadFile(String keyName, File fileToUpload);

    byte[] downloadFile(String fileName) throws IOException;

    List<String> getFilesInBucket();
}
