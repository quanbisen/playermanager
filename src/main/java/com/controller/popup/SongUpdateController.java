package com.controller.popup;

import com.controller.content.TabSongController;
import com.pojo.Song;
import com.service.LoadOnlineImageService;
import com.service.UpdateSongService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

/**
 * @author super lollipop
 * @date 20-2-23
 */

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

    /**选中的歌曲对象模型*/
    private Song selectedSong;

    public TextField getTfTitle() {
        return tfTitle;
    }

    public TextField getTfArtist() {
        return tfArtist;
    }

    public TextField getTfAlbum() {
        return tfAlbum;
    }

    public void setTabSongController(TabSongController tabSongController) {
        this.tabSongController = tabSongController;
    }

    public void initialize(){
        Platform.runLater(()->{
            btnUpload.requestFocus();
            selectedSong = tabSongController.getTableViewSong().getSelectionModel().getSelectedItem(); //赋值为选中的歌曲对象
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
            LoadOnlineImageService loadOnlineImageService = new LoadOnlineImageService();
            loadOnlineImageService.setImageSize(150,150);
            if (selectedSong.getAlbumObject() != null && selectedSong.getAlbumObject().getImageURL() != null){  //优先加载收录专辑的图片
                loadOnlineImageService.setPreferURI(selectedSong.getAlbumObject().getImageURL());
            }else if (selectedSong.getAlbumURL() != null && !selectedSong.getAlbumURL().equals("")){
                loadOnlineImageService.setPreferURI(selectedSong.getAlbumURL());
            }
            loadOnlineImageService.setOnSucceeded(event -> ivAlbum.setImage(loadOnlineImageService.getValue()));
            loadOnlineImageService.start();
        });

    }

    @FXML
    public void onClickedUpdate(ActionEvent actionEvent) {
        validateInput();
        if(tfAlbum.getText().equals(selectedSong.getAlbumName()) && tfTitle.getText().equals(selectedSong.getName())
                &&tfArtist.getText().equals(selectedSong.getAlbumObject().getName())){  //如果没有修改，直接关闭
            tfArtist.getScene().getWindow().hide();
        }else { //否则，启动服务更新
            UpdateSongService updateSongService = new UpdateSongService();
            progressIndicator.visibleProperty().bind(updateSongService.runningProperty());
            updateSongService.start();
        }

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
