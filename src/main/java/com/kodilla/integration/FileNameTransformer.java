package com.kodilla.integration;


import java.nio.file.Paths;

public class FileNameTransformer {
    public String transformFileName(String filePath) {
        return Paths.get(filePath).getFileName().toString();
    }
}
