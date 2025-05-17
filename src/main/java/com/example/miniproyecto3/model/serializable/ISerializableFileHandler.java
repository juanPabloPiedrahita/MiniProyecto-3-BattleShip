package com.example.miniproyecto3.model.serializable;

import java.io.Serializable;

public interface ISerializableFileHandler {
    void serialize(String filename, Object element);
    Object deserialize(String filename);
}
