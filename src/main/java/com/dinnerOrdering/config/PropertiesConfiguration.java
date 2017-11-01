package com.dinnerOrdering.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by vlza1116 on 05.05.2017.
 */
public class PropertiesConfiguration {

    public static Properties getProperties(InputStream inputStream) {
        Properties properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
