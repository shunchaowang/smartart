package com.swang.smartart.core.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads runtime properties for spring.
 * Created by swang on 3/4/2015.
 */
public class PropertiesLoader {

    public Properties load(String fileName) {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = findFile(fileName);
            prop.load(inputStream);
        } catch (IOException ignore) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
            }
        }
        return prop;
    }

    /**
     * Loads file from user.dir, classpath and source dir orderly.
     *
     * @param fileName
     * @return
     * @throws java.io.FileNotFoundException
     */
    private InputStream findFile(String fileName) throws FileNotFoundException {
        InputStream inputStream = findInWorkingDirectory(fileName);
        if (inputStream == null) inputStream = findInClasspath(fileName);
        if (inputStream == null) inputStream = findInSourceDirectory(fileName);
        if (inputStream == null)
            throw new FileNotFoundException(String.format("File %s not found", fileName));
        return inputStream;
    }

    /**
     * Loads file from src/main/resources/ folder of the project.
     *
     * @param fileName
     * @return
     * @throws java.io.FileNotFoundException
     */
    private InputStream findInSourceDirectory(String fileName) throws FileNotFoundException {
        return new FileInputStream("src/main/resources/" + fileName);
    }

    /**
     * Loads file from java thread classpath.
     *
     * @param fileName
     * @return
     */
    private InputStream findInClasspath(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    /**
     * Loads file from user.dir system environment variable.
     *
     * @param fileName
     * @return
     */
    private InputStream findInWorkingDirectory(String fileName) {
        try {
            return new FileInputStream(System.getProperty("user.dir") + fileName);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
