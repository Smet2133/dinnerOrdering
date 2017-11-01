package com.dinnerOrdering.config;

import com.dinnerOrdering.db.DbController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
//import ru.k0ba32.db.DbController;

import java.io.InputStream;
import java.util.Properties;

@Configuration
public class BeanConfiguration {
    public static final String PROPERTIES_FILE_NAME = "config.properties";
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);;
    Properties properties = PropertiesConfiguration.getProperties(inputStream);

    @Bean(name = "dataSource")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(properties.getProperty("db.driver"));
        driverManagerDataSource.setUrl(properties.getProperty("db.url"));
        driverManagerDataSource.setUsername(properties.getProperty("db.username"));
        driverManagerDataSource.setPassword(properties.getProperty("db.password"));
        return driverManagerDataSource;
    }

    @Bean(name="userDetailsService")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public UserDetailsService userDetailsService(){
        JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
        jdbcImpl.setDataSource(dataSource());
        jdbcImpl.setUsersByUsernameQuery(properties.getProperty("db.query.login.user"));
        jdbcImpl.setAuthoritiesByUsernameQuery(properties.getProperty("db.query.login.role"));
        return jdbcImpl;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public DbController db() {
        return new DbController();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Properties properties() {
        return properties;
    }
}
