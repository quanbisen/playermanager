package com.service;

import com.config.ServerConfig;
import com.controller.popup.SongUpdateController;
import com.controller.content.TabSongController;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author super lollipop
 * @date 20-2-23
 */
@Service
@Scope("prototype")
public class UpdateSongService extends javafx.concurrent.Service<Void> {

    @Autowired
    private SongUpdateController songUpdateController;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TabSongController tabSongController;

    @Override
    protected Task<Void> createTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String responseString = update();
                if (responseString.equals("success")){
                    Platform.runLater(()->{
                        songUpdateController.getTfAlbum().getScene().getWindow().hide();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("更新成功");
                        alert.showAndWait();
                        tabSongController.updateTable();
                    });
                }else {
                    Platform.runLater(()->{
                        songUpdateController.getTfAlbum().getScene().getWindow().hide();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("更新失败");
                        alert.showAndWait();
                    });
                }
                return null;
            }
        };
        return task;
    }

    private String update() throws IOException {
        String url = applicationContext.getBean(ServerConfig.class).getSongURL() + "/update";
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                addTextBody("id",String.valueOf(songUpdateController.getSelectedSong().getId()), ContentType.create("text/plain", Charset.forName("UTF-8"))).
                addTextBody("name",songUpdateController.getTfTitle().getText(),ContentType.create("text/plain", Charset.forName("UTF-8"))).
                addTextBody("singer",songUpdateController.getTfArtist().getText(),ContentType.create("text/plain", Charset.forName("UTF-8"))).
                addTextBody("album",songUpdateController.getTfAlbum().getText(),ContentType.create("text/plain", Charset.forName("UTF-8")));
        return HttpClientUtils.executePost(url,multipartEntityBuilder.build());
    }
}
