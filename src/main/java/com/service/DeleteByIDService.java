package com.service;

import com.config.Category;
import com.config.ServerConfig;
import com.util.HttpClientUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DeleteByIDService extends Service<String> {

    private int id;

    private Category category;

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
                        url = ServerConfig.getInstance().getSingerURL();
                        break;
                    }
                    case Album:{
                        url = ServerConfig.getInstance().getAlbumURL();
                        break;
                    }
                    case Song:{
                        url = ServerConfig.getInstance().getSongURL();
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
