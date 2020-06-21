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
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-23
 */

public class InsertSongService extends Service<Void> {

    private SongInsertController songInsertController;

    private TabSongController tabSongController;

    public void setSongInsertController(SongInsertController songInsertController) {
        this.songInsertController = songInsertController;
    }

    public void setTabSongController(TabSongController tabSongController){
        this.tabSongController = tabSongController;
    }

    @Override
    protected Task<Void> createTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = ServerConfig.getInstance().getSongURL() + "/insert";
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
    private String upload(String url, Song song , File songFile, File lyricFile, byte[] albumBytes) throws IOException {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addTextBody("song", JSONObject.toJSONString(song),ContentType.create("text/plain",Charset.forName("utf-8"))).
                addBinaryBody("songBytes", Files.readAllBytes(songFile.toPath()),ContentType.create("multipart/form-data",Charset.forName("utf-8")),songFile.getName()).
                addBinaryBody("lyricBytes", Files.readAllBytes(lyricFile.toPath()),ContentType.create("multipart/form-data",Charset.forName("utf-8")),lyricFile.getName()).
                addBinaryBody("albumBytes", albumBytes,ContentType.create("multipart/form-data",Charset.forName("utf-8")),"");
        return HttpClientUtils.executePost(url,multipartEntityBuilder.build());
    }
}
