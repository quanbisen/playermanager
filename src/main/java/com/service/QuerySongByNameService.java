package com.service;

import com.alibaba.fastjson.JSON;
import com.config.ServerConfig;
import com.controller.popup.SongQueryController;
import com.pojo.Song;
import com.util.HttpClientUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Service
@Scope("prototype")
public class QuerySongByNameService extends javafx.concurrent.Service<ObservableList<Song>> {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private SongQueryController songQueryController;

    @Override
    protected Task<ObservableList<Song>> createTask() {
        Task<ObservableList<Song>> task = new Task<ObservableList<Song>>() {
            @Override
            protected ObservableList<Song> call() throws Exception {
                System.out.println(applicationContext.getBean(ServerConfig.class).getSongURL() + "/query/" + songQueryController.getTfName().getText());
                String responseString = HttpClientUtils.executeGet(applicationContext.getBean(ServerConfig.class).getSongURL() + "/query/" + songQueryController.getTfName().getText());
                List<Song> songList = JSON.parseArray(responseString, Song.class);
                if (songList != null && songList.size() > 0){
                    ObservableList<Song> observableList = FXCollections.observableArrayList();
                    observableList.addAll(songList);
                    return observableList;
                }else {
                    return null;
                }
            }
        };
        return task;
    }
}
