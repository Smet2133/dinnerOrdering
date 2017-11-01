package com.dinnerOrdering.config;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.InputStream;
import java.util.Properties;

public class DriverManagerMy {
    public static final String PROPERTIES_FILE_NAME = "config.properties";
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);;
    Properties properties = PropertiesConfiguration.getProperties(inputStream);

    public DriverManagerDataSource dataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(properties.getProperty("db.driver"));
        driverManagerDataSource.setUrl(properties.getProperty("db.url"));
        driverManagerDataSource.setUsername(properties.getProperty("db.username"));
        driverManagerDataSource.setPassword(properties.getProperty("db.password"));
        return driverManagerDataSource;
    }

}
