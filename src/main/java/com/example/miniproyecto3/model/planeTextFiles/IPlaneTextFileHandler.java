package com.example.miniproyecto3.model.planeTextFiles;

import java.io.IOException;

/**
 * Interface that defines the contract for reading from and writing to plain text files.

 * Implementations of this interface provide methods to persist game-related data in textual format,
 * such as logs, scoreboards, or configurations.

 * It serves as a base abstraction to allow flexible file handling mechanisms.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 3.0
 */
public interface IPlaneTextFileHandler {

    /**
     * Writes the given content to the specified plain text file.
     *
     * @param filename the name of the file (including path) to write to.
     * @param content the content to be written to the file.
     * @throws IOException if an I/O error occurs during writing.
     */
    void write(String filename, String content) throws IOException;

    /**
     * Reads and returns all lines from the specified plain text file.
     *
     * @param filename the name of the file (including path) to read from.
     * @return an Array of strings, each representing a line in the file.
     */
    String[] read(String filename);
}
