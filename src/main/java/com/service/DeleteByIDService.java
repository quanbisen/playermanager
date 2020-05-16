package com.service;

import com.config.Category;
import com.config.ServerConfig;
import com.util.HttpClientUtils;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class DeleteByIDService extends javafx.concurrent.Service<String> {

    private int id;

    private Category category;

    private ServerConfig serverConfig;

    @Autowired
    public void constructor(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    protected Task<String> createTask() {
        Task<String> deleteTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                String url = null;
                switch (category){
                    case Singer:{
                        url = serverConfig.getSingerURL();
                        break;
                    }
                    case Album:{
                        url = serverConfig.getAlbumURL();
                        break;
                    }
                    case Song:{
                        url = serverConfig.getSongURL();
                        break;
                    }
                    default:
                }
                url = url + "/delete/" + id;
                return HttpClientUtils.executeDelete(url);
            }
        };
        return deleteTask;
    }
}
