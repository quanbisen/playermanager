package com.service;

import com.alibaba.fastjson.JSONObject;
import com.config.ServerConfig;
import com.controller.content.TabAlbumController;
import com.controller.popup.AlbumUpdateController;
import com.pojo.Album;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

@Service
@Scope("prototype")
public class UpdateAlbumService extends javafx.concurrent.Service<Void> {

    private AlbumUpdateController albumUpdateController;

    private ServerConfig serverConfig;

    private TabAlbumController tabAlbumController;


    @Autowired
    public void constructor(AlbumUpdateController albumUpdateController,ServerConfig serverConfig,TabAlbumController tabAlbumController){
        this.albumUpdateController = albumUpdateController;
        this.serverConfig = serverConfig;
        this.tabAlbumController = tabAlbumController;
    }

    @Override
    protected Task<Void> createTask() {
        Task<Void> updateAlbumTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //获得编辑的数据
                String description = albumUpdateController.getTaDescription().getText().trim();
                File imageFile = albumUpdateController.getImageFile();
                //获取原来的专辑信息对象
                Album album = tabAlbumController.getTableViewAlbum().getSelectionModel().getSelectedItem();
                if (!description.equals(album.getDescription()) || imageFile != null){    //判断是否需要执行更新操作，如果true则需要
                    String url = serverConfig.getAlbumURL() + "/update";
                    if (!StringUtils.isEmpty(description)){
                        album.setDescription(description);
                    }
                    //建立多类型实体
                    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                            addTextBody("album", JSONObject.toJSONString(album), ContentType.create("text/plain", Charset.forName("utf-8"))).
                            addBinaryBody("imageBytes", Files.readAllBytes(imageFile.toPath()),ContentType.create("multipart/form-data",Charset.forName("utf-8")),imageFile.getName());
                    String responseString = HttpClientUtils.executePost(url,multipartEntityBuilder.build());
                    Platform.runLater(()->{
                        if (responseString.equals("success")){
                            AlertUtils.showInformation("更新专辑成功");
                            tabAlbumController.updateTable();
                        }else {
                            AlertUtils.showInformation("更新专辑失败");
                        }
                        ((Stage)albumUpdateController.getTaDescription().getScene().getWindow()).close();
                        albumUpdateController.destroy();
                    });
                }
                return null;
            }
        };
        return updateAlbumTask;
    }
}
