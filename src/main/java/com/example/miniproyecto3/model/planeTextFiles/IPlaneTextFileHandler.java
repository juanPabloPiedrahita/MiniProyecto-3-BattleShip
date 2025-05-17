package com.example.miniproyecto3.model.planeTextFiles;

public interface IPlaneTextFileHandler {
    void write(String filename, String content);
    String[] read(String filename);
}
