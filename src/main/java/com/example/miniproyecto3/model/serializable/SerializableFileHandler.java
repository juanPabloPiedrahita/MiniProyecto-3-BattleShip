package com.example.miniproyecto3.model.serializable;

import java.io.*;

public class SerializableFileHandler implements ISerializableFileHandler{
    @Override
    public void serialize(String filename, Object element) {
        try(ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream(filename))){
            obs.writeObject(element);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

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
