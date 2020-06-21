package com.service;

import com.alibaba.fastjson.JSONObject;
import com.config.ServerConfig;
import com.controller.content.TabAlbumController;
import com.pojo.Album;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class InsertAlbumService extends Service<Void> {

    private Album album;

    private File imageFile;

    private TabAlbumController tabAlbumController;

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public void setTabAlbumController(TabAlbumController tabAlbumController) {
        this.tabAlbumController = tabAlbumController;
    }

    @Override
    protected Task<Void> createTask(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String url = ServerConfig.getInstance().getAlbumURL() + "/insert";
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                        addTextBody("album", JSONObject.toJSONString(album), ContentType.create("application/json", Charset.forName("utf-8"))).
                        addBinaryBody("imageBytes", Files.readAllBytes(imageFile.toPath()),ContentType.create("multipart/form-data",Charset.forName("utf-8")),imageFile.getName());
                String responseString = HttpClientUtils.executePost(url,multipartEntityBuilder.build());
                Platform.runLater(()->{
                    if (responseString.equals("success")){
                        AlertUtils.showInformation("新增专辑成功");
                        tabAlbumController.updateTable();
                    }else {
                        AlertUtils.showError("新增专辑失败");
                    }
                });
                return null;
            }
        };
        return task;
    }
}
