package com.example.miniproyecto3.model.serializable;

/**
 * Defines the contract for serializing and deserializing objects
 * to and from files using Java's standard serialization mechanism.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 3.0
 */
public interface ISerializableFileHandler {

    /**
     * Serializes the specified object and writes it to a file with the given filename.
     *
     * @param filename The path of the file where the object will be saved.
     * @param element The object to serialize. It should implement {@link java.io.Serializable}.
     */
    void serialize(String filename, Object element);

    /**
     * Deserializes an object from the specified file and returns it.
     *
     * @param filename The path of the file to read the object from.
     * @return The deserialized object, or null if an error occurs during deserialization.
     */
    Object deserialize(String filename);
}
