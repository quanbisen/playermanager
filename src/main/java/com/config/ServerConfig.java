package com.config;

import java.io.*;
import java.util.Properties;

/**
 * @author super lollipop
 * @date 20-2-22
 */
public class ServerConfig {

    private String server;

    private static ServerConfig serverConfig;

    public static ServerConfig getInstance() throws IOException {
        if (serverConfig == null){
            serverConfig = new ServerConfig();
        }
        return serverConfig;
    }

    public ServerConfig() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("config/server-config.properties")));
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
