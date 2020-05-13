package com.service;

import com.alibaba.fastjson.JSONObject;
import com.config.ServerConfig;
import com.controller.content.TabAlbumController;
import com.pojo.Album;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

@Service
@Scope("prototype")
public class InsertAlbumService extends javafx.concurrent.Service<Void>{

    private Album album;

    private File imageFile;

    private Button btnCancel;

    private ServerConfig serverConfig;

    private TabAlbumController tabAlbumController;

    @Autowired
    public void constructor(ServerConfig serverConfig,TabAlbumController tabAlbumController){
        this.serverConfig = serverConfig;
        this.tabAlbumController = tabAlbumController;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    @Override
    protected Task<Void> createTask(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String url = serverConfig.getAlbumURL() + "/insert";
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                        addTextBody("album", JSONObject.toJSONString(album), ContentType.create("application/json", Charset.forName("utf-8"))).
                        addBinaryBody("imageBytes", Files.readAllBytes(imageFile.toPath()),ContentType.create("multipart/form-data",Charset.forName("utf-8")),imageFile.getName());
                String responseString = HttpClientUtils.executePost(url,multipartEntityBuilder.build());
                Platform.runLater(()->{
                    if (responseString.equals("success")){
                        AlertUtils.showInformation("新增专辑成功");
                        tabAlbumController.updateTable();
                        ((Stage)btnCancel.getScene().getWindow()).close();
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
