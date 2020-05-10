package com.service;

import com.config.ServerConfig;
import com.controller.popup.SongInsertController;
import com.controller.content.TabSongController;
import com.pojo.Song;
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

/**
 * @author super lollipop
 * @date 20-2-23
 */
@Service
@Scope("prototype")
public class InsertSongService extends javafx.concurrent.Service<Void> {

    @Autowired
    private SongInsertController songInsertController;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TabSongController tabSongController;

    @Override
    protected Task<Void> createTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String url = applicationContext.getBean(ServerConfig.class).getSongURL() + "/upload";
                Song song = new Song();
                song.setName(songInsertController.getTfTitle().getText());
                song.setSinger(songInsertController.getTfArtist().getText());
                song.setAlbum(songInsertController.getTfAlbum().getText());
                song.setTotalTime(songInsertController.getLabTotalTime().getText());
                song.setSize(songInsertController.getLabSize().getText());
                String responseString = upload(url,songInsertController.getSongFile(),song,songInsertController.getLyricFile(),songInsertController.getAlbumBytes());
                if (responseString.equals("success")){  //返回的信息是success，证明上传成功了。
                    Platform.runLater(()->{
                        songInsertController.getLabTotalTime().getScene().getWindow().hide();   //关闭窗口
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("新增歌曲成功");
                        alert.showAndWait();
                        tabSongController.updateTable();
                    });
                }else {
                    Platform.runLater(()->{
                        songInsertController.getLabTotalTime().getScene().getWindow().hide();   //关闭窗口
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("新增歌曲失败");
                        alert.showAndWait();
                    });
                }
                return null;
            }
        };
        return task;
    }

    /**上传文件的函数
     * @param url 服务器发送地址*/
    private String upload(String url, File songFile, Song song , File lyricFile, byte[] bytes){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);  //"http://127.0.0.1:8080/OnlineExam_war_exploded/Student/HandleUploadImage"
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                addBinaryBody("songFile",songFile).     //歌曲文件
                addTextBody("name",song.getName(), ContentType.create("text/plain", Charset.forName("UTF-8"))).
                addTextBody("singer",song.getSinger(),ContentType.create("text/plain", Charset.forName("UTF-8"))).
                addTextBody("album",song.getAlbum(),ContentType.create("text/plain", Charset.forName("UTF-8"))).
                addTextBody("totalTime",song.getTotalTime(),ContentType.create("text/plain", Charset.forName("UTF-8"))).
                addTextBody("size",song.getSize(),ContentType.create("text/plain", Charset.forName("UTF-8"))).
                addBinaryBody("lyricFile",lyricFile).
                addBinaryBody("bytes",bytes, ContentType.DEFAULT_BINARY,"fileName.jpg");
        httpPost.setEntity(multipartEntityBuilder.build());
        try {
            HttpResponse response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
