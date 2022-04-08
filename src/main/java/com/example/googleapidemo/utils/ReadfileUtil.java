package com.example.googleapidemo.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public class ReadfileUtil {
    private static final String CLASSPATH_PREFIX = "classpath:";

    public static InputStream readFrom(String file) throws FileNotFoundException {
        if (file.startsWith(CLASSPATH_PREFIX)) {
            return ReadfileUtil.class.getResourceAsStream(file.substring(CLASSPATH_PREFIX.length()));
        } else {
            return new FileInputStream(file);
        }
    }
}
