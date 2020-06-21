package com.service;

import com.alibaba.fastjson.JSON;
import com.config.ServerConfig;
import com.controller.popup.SongQueryController;
import com.pojo.Song;
import com.util.HttpClientUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-22
 */

public class QuerySongByNameService extends Service<ObservableList<Song>> {

    private SongQueryController songQueryController;

    @Override
    protected Task<ObservableList<Song>> createTask() {
        Task<ObservableList<Song>> task = new Task<ObservableList<Song>>() {
            @Override
            protected ObservableList<Song> call() throws Exception {
//                String responseString = HttpClientUtils.executeGet(applicationContext.getBean(ServerConfig.class).getSongURL() + "/query/" + songQueryController.getTfName().getText());
//                List<Song> songList = JSON.parseArray(responseString, Song.class);
//                if (songList != null && songList.size() > 0){
//                    ObservableList<Song> observableList = FXCollections.observableArrayList();
//                    observableList.addAll(songList);
//                    return observableList;
//                }else {
//                    return null;
//                }
                return null;
            }
        };
        return task;
    }
}
