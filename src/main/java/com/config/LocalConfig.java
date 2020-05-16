package com.config;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class LocalConfig {

    private Path configPath;

    public Path getConfigPath() {
        return configPath;
    }

    public LocalConfig() throws IOException {
        /**server part*/
        BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("config/server-config.properties")));
        Properties properties = new Properties();
        properties.load(in);

        /**local config part*/
        Path configParent;
        Path configDir;
        String appData = System.getenv("APPDATA");
        if (appData != null) {
            configParent = Paths.get(appData);
        } else {
            configParent = Paths.get(System.getProperty("user.home"));
        }
        configDir = configParent.resolve("playermanager");
        configPath = Files.createDirectories(configDir);
    }
}
