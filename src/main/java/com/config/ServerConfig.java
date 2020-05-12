package com.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Component
@Scope("singleton")
public class ServerConfig {

    private String server;

    public ServerConfig() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("config/server-config.properties")));
        Properties properties = new Properties();
        properties.load(in);
        server = properties.getProperty("server");
    }

    public String getSongURL() {
        return server + "/song";
    }

    public String getSingerURL(){
        return server + "/singer";
    }

    public String getAlbumURL(){
        return server + "/album";
    }
}
