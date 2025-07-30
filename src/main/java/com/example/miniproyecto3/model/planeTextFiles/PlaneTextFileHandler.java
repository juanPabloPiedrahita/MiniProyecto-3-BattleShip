package com.example.miniproyecto3.model.planeTextFiles;

import java.io.*;

/**
 * Implements the IPlaneTextFileHandler interface to provide basic
 * functionality for reading from and writing to plain text files.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 3.0
 * @see IPlaneTextFileHandler
 */
public class PlaneTextFileHandler implements IPlaneTextFileHandler{

    /**
     * Writes the specified content to the given file.
     * If the file does not exist, it will be created.
     *
     * @param filename The name of the file to write to.
     * @param content The content to be written into the file.
     * @throws IOException If an I/O error occurs during writing.
     */
    @Override
    public void write(String filename, String content) throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write(content);
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads the content of the specified file and returns it as an Array of strings.
     * Each line is concatenated and separated by a comma.
     *
     * @param filename The name of the file to read from.
     * @return A string Array containing the content split by commas.
     */
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
