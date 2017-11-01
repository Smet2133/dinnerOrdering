package com.dinnerOrdering;

import com.dinnerOrdering.config.PropertiesConfiguration;
import com.dinnerOrdering.scheduling.Scheduling;
import org.springframework.boot.*;
import org.springframework.stereotype.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class PreRun implements CommandLineRunner {

    public static final String PROPERTIES_FILE_NAME = "config.properties";
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);;
    Properties properties = PropertiesConfiguration.getProperties(inputStream);

    public void run(String... args) {
     /*   File inTomcat = new File(properties.getProperty("excel.path"));
        try {
            inTomcat.getParentFile().mkdirs();
            inTomcat.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        new Scheduling().startScheduling();
    }

}