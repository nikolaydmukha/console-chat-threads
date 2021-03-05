package ru.home.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private Properties props = new Properties();
    private static AppConfig instance;

    private AppConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error during loading app.properties! " + ex.getMessage());
        }
    }
    public static AppConfig getInstance(){
        if(instance == null){
            instance = new AppConfig();
        }
        return instance;
    }

    public int getPort() {
        return Integer.parseInt(getParam("server.port"));
    }

    public String getIP() {
        return getParam("server.host");
    }

    public String getLog() {
        return getParam("log.file");
    }

    private String getParam(String param) {
        return props.getProperty(param);
    }

}