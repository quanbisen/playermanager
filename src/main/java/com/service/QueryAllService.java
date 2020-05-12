package com.service;

import com.alibaba.fastjson.JSON;
import com.config.ServerConfig;
import com.pojo.Album;
import com.pojo.Singer;
import com.pojo.Song;
import com.util.HttpClientUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Scope("prototype")
public class QueryAllService extends javafx.concurrent.Service<ObservableList> {

    private ServerConfig serverConfig;

    /**枚举当前要查询的是哪个种类*/
    private Category categoryEnum;

    @Autowired
    public void constructor(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
    }

    public void setCategoryEnum(Category categoryEnum) {
        this.categoryEnum = categoryEnum;
    }

    @Override
    protected Task<ObservableList> createTask() {
        Task<ObservableList> task = new Task<ObservableList>() {
            @Override
            protected ObservableList call() throws Exception {
                String url = null;
                switch (categoryEnum){
                    case Singer:{
                        url = serverConfig.getSingerURL() + "/queryAll";
                        break;
                    }
                    case Album:{
                        url = serverConfig.getAlbumURL() + "/queryAll";
                        break;
                    }
                    case Song:{
                        url = serverConfig.getSongURL() + "/queryAll";
                        break;
                    }
                    default:
                }
                String responseString = HttpClientUtils.executeGet(url);
                List list = null;
                switch (categoryEnum){
                    case Singer:{
                        list = JSON.parseArray(responseString,Singer.class);
                        break;
                    }
                    case Album:{
                        list = JSON.parseArray(responseString, Album.class);
                        break;
                    }
                    case Song:{
                        list = JSON.parseArray(responseString, Song.class);
                        break;
                    }
                    default:
                }
                ObservableList observableList = FXCollections.observableArrayList();
                observableList.addAll(list);
                return observableList;
            }
        };
        return task;
    }
}
