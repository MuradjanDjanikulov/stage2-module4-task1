package com.mjc.stage2.impl;

import com.mjc.stage2.ConnectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class H2ConnectionFactory implements ConnectionFactory {

    private static final String PROPERTIES_FILE = "h2database.properties";

    static {
        loadDatabaseDriver();
    }

    private static void loadDatabaseDriver() {
        try {
            Class.forName(getProperty("jdbc_driver"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading H2 database driver.", e);
        }
    }

    private static String getProperty(String propertyName) {
        Properties properties = new Properties();
        try (InputStream input = H2ConnectionFactory.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error reading properties file.", e);
        }
        return properties.getProperty(propertyName);
    }

    @Override
    public Connection createConnection() {
        try {
            return DriverManager.getConnection(
                    getProperty("db_url"),
                    getProperty("user"),
                    getProperty("password")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error creating H2 database connection.", e);
        }
    }
}


