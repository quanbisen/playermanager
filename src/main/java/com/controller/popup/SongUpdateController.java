package com.controller.popup;

import com.controller.content.TabSongController;
import com.pojo.Song;
import com.service.LoadOnlineImageService;
import com.service.UpdateSongService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

/**
 * @author super lollipop
 * @date 20-2-23
 */
@Controller
public class SongUpdateController {


    @FXML
    private ImageView ivAlbum;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfArtist;

    @FXML
    private TextField tfAlbum;

    @FXML
    private Label labTotalTime;

    @FXML
    private Label labSize;

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnCancel;

    @FXML
    private Label labCollectAlbum;

    @FXML
    private ProgressIndicator progressIndicator;

    private TabSongController tabSongController;

    private ApplicationContext applicationContext;

    @Autowired
    public void constructor(ApplicationContext applicationContext,TabSongController tabSongController){
        this.applicationContext = applicationContext;
        this.tabSongController = tabSongController;
    }

    public TextField getTfTitle() {
        return tfTitle;
    }

    public TextField getTfArtist() {
        return tfArtist;
    }

    public TextField getTfAlbum() {
        return tfAlbum;
    }

    public void initialize(){
        Platform.runLater(()->btnUpload.requestFocus());
        Song selectedSong = tabSongController.getTableViewSong().getSelectionModel().getSelectedItem();
        tfTitle.setText(selectedSong.getName());    //设置歌曲名称
        tfAlbum.setText(selectedSong.getAlbumName());   //设置歌曲填写的专辑
        if (selectedSong.getSingerList() != null){      //设置歌曲的歌手
            StringBuilder singer = new StringBuilder();
            for (int i = 0; i < selectedSong.getSingerList().size(); i++) {
                singer.append(selectedSong.getSingerList().get(i).getName());
                if (i != selectedSong.getSingerList().size() -1){
                    singer.append("/");
                }
            }
            tfArtist.setText(singer.toString());
        }
        if (selectedSong.getAlbumObject() != null){
            tfAlbum.setText(selectedSong.getAlbumObject().getName());
        }
        labSize.setText(selectedSong.getSize());
        labTotalTime.setText(selectedSong.getTotalTime());
        //加载专辑图片的服务
        LoadOnlineImageService loadOnlineImageService = applicationContext.getBean(LoadOnlineImageService.class);
        loadOnlineImageService.setImageSize(150,150);
        if (selectedSong.getAlbumObject() != null && selectedSong.getAlbumObject().getImageURL() != null){  //优先加载收录专辑的图片
            loadOnlineImageService.setOptimizeURI(selectedSong.getAlbumObject().getImageURL());
        }else if (!StringUtils.isEmpty(selectedSong.getAlbumURL())){
            loadOnlineImageService.setOptimizeURI(selectedSong.getAlbumURL());
        }
        loadOnlineImageService.setOnSucceeded(event -> ivAlbum.setImage(loadOnlineImageService.getValue()));
        loadOnlineImageService.start();
    }

    @FXML
    public void onClickedUpdate(ActionEvent actionEvent) {
        validateInput();
//        if(tfAlbum.getText().equals(selectedSong.getAlbum()) && tfTitle.getText().equals(selectedSong.getName())
//                &&tfArtist.getText().equals(selectedSong.getAlbum())){  //如果没有修改，直接关闭
//            tfArtist.getScene().getWindow().hide();
//
//        }else { //否则，启动服务更新
//            UpdateSongService updateSongService = applicationContext.getBean(UpdateSongService.class);
//            progressIndicator.visibleProperty().bind(updateSongService.runningProperty());
//            updateSongService.start();
//        }

    }

    private void validateInput(){
        if (tfTitle.getText().equals("") || tfArtist.getText().equals("") || tfAlbum.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("信息输入不允许为空");
            alert.showAndWait();
        }
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        btnCancel.getScene().getWindow().hide();
    }
}
