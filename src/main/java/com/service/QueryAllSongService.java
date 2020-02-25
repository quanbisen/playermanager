package com.service;

import com.alibaba.fastjson.JSON;
import com.config.ServerConfig;
import com.pojo.Song;
import com.util.HttpClientUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Service
@Scope("prototype")
public class QueryAllSongService extends javafx.concurrent.Service<ObservableList<Song>> {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    protected Task<ObservableList<Song>> createTask() {
        Task<ObservableList<Song>> task = new Task<ObservableList<Song>>() {
            @Override
            protected ObservableList<Song> call() throws Exception {
                String string;
                try {
                    string = HttpClientUtils.executeGet(applicationContext.getBean(ServerConfig.class).getSongURL() + "/queryAll");
                    List<Song> songList = JSON.parseArray(string, Song.class);
                    if (songList != null && songList.size() > 0){
                        ObservableList<Song> observableList = FXCollections.observableArrayList();
                        observableList.addAll(songList);
                        return observableList;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return task;
    }
}
