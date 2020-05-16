package com.service;

import com.alibaba.fastjson.JSONObject;
import com.config.ServerConfig;
import com.controller.popup.SongInsertController;
import com.controller.content.TabSongController;
import com.pojo.Album;
import com.pojo.Singer;
import com.pojo.Song;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-23
 */
@Service
@Scope("prototype")
public class InsertSongService extends javafx.concurrent.Service<Void> {

    private SongInsertController songInsertController;

    private TabSongController tabSongController;

    private ApplicationContext applicationContext;

    @Autowired
    public void constructor(SongInsertController songInsertController,TabSongController tabSongController,ApplicationContext applicationContext){
        this.songInsertController = songInsertController;
        this.tabSongController = tabSongController;
        this.applicationContext = applicationContext;
    }

    @Override
    protected Task<Void> createTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String url = applicationContext.getBean(ServerConfig.class).getSongURL() + "/insert";
                Song song = new Song();
                song.setName(songInsertController.getTfTitle().getText());
                song.setSingerList((List<Singer>) songInsertController.getTfArtist().getUserData());
                if (songInsertController.getTfAlbum().getUserData() != null){
                    song.setAlbumObject((Album) songInsertController.getTfAlbum().getUserData());
                }
                song.setAlbumName(songInsertController.getTfAlbum().getText());
                song.setTotalTime(songInsertController.getLabTotalTime().getText());
                song.setSize(songInsertController.getLabSize().getText());
                String responseString = upload(url,song,songInsertController.getSongFile(),songInsertController.getLyricFile(),songInsertController.getAlbumFile());
                Platform.runLater(()->{
                    songInsertController.getLabTotalTime().getScene().getWindow().hide();   //关闭窗口
                    if (responseString.equals("success")){  //返回的信息是success，证明上传成功了。
                        AlertUtils.showInformation("新增歌曲成功");
                        tabSongController.updateTable();
                    }else {
                        AlertUtils.showInformation("新增歌曲失败");
                    }
                });
                return null;
            }
        };
        return task;
    }

    /**上传文件的函数
     * @param url 服务器发送地址
     * @return responseString */
    private String upload(String url, Song song , File songFile, File lyricFile, File albumFile) throws IOException {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addTextBody("song", JSONObject.toJSONString(song),ContentType.create("text/plain",Charset.forName("utf-8"))).
                addBinaryBody("songBytes", Files.readAllBytes(songFile.toPath()),ContentType.create("multipart/form-data",Charset.forName("utf-8")),songFile.getName()).
                addBinaryBody("lyricBytes", Files.readAllBytes(lyricFile.toPath()),ContentType.create("multipart/form-data",Charset.forName("utf-8")),lyricFile.getName()).
                addBinaryBody("albumBytes", Files.readAllBytes(albumFile.toPath()),ContentType.create("multipart/form-data",Charset.forName("utf-8")),albumFile.getName());
        return HttpClientUtils.executePost(url,multipartEntityBuilder.build());
    }
}
