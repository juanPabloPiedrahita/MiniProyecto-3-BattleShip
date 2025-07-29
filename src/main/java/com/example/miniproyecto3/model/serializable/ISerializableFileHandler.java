package com.example.miniproyecto3.model.serializable;

public interface ISerializableFileHandler {
    void serialize(String filename, Object element);
    Object deserialize(String filename);
}
