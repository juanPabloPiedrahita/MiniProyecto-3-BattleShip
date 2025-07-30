package com.example.miniproyecto3.model.serializable;

import java.io.*;

/**
 * Implements the {@link ISerializableFileHandler} interface to provide
 * basic functionality for serializing and deserializing Java objects
 * to and from files using {@link ObjectOutputStream} and {@link ObjectInputStream}.

 * This class handles possible I/O and class resolution exceptions internally,
 * printing stack traces in the debugging.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 3.0
 * @see ISerializableFileHandler
 */
public class SerializableFileHandler implements ISerializableFileHandler{

    /**
     * Serializes a given object and writes it to a file specified by the filename.
     * The object passed must implement the {@link Serializable} interface.
     *
     * @param filename The name or path of the file where the object will be written.
     * @param element The object to be serialized.
     */
    @Override
    public void serialize(String filename, Object element) {
        try(ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream(filename))){
            obs.writeObject(element);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Deserializes an object from the file specified by the filename.
     *
     * @param filename The name or path of the file from which the object will be read.
     * @return The deserialized object, or null if deserialization fails.
     */
    @Override
    public Object deserialize(String filename) {
        try(ObjectInputStream inp = new ObjectInputStream(new FileInputStream(filename))){
            return inp.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
