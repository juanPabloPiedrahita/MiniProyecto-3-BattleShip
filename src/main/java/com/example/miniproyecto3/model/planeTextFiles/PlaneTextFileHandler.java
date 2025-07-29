package com.example.miniproyecto3.model.planeTextFiles;

import java.io.*;

public class PlaneTextFileHandler implements IPlaneTextFileHandler{
    @Override
    public void write(String filename, String content) throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write(content);
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String[] read(String filename) {
        StringBuilder content = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = reader.readLine()) != null){
                content.append(line).append(",");
            }
        }catch (IOException e ){
            e.printStackTrace();
        }
        return content.toString().split(",");
    }
}
