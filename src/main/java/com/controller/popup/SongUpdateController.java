package com.controller.popup;

import com.controller.tabcontent.TabSongController;
import com.pojo.Song;
import com.service.UpdateSongService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;

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
    private Button btnUpload1;

    @FXML
    private Button btnCancel1;

    @FXML
    private ProgressIndicator progressIndicator;

    @Resource
    private TabSongController tabSongController;

    @Resource
    private ApplicationContext applicationContext;

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

    public Song getSelectedSong() {
        return selectedSong;
    }

    public void initialize(){
        selectedSong = tabSongController.getTableViewSong().getSelectionModel().getSelectedItem();
        tfTitle.setText(selectedSong.getName());
        tfArtist.setText(selectedSong.getSinger());
        tfAlbum.setText(selectedSong.getAlbum());
        Image albumImage = new Image(selectedSong.getAlbumURL(),150,150,true,true);
        if (!albumImage.isError()){
            ivAlbum.setImage(albumImage);
        }
        labSize.setText(selectedSong.getSize());
        labTotalTime.setText(selectedSong.getTotalTime());
    }

    @FXML
    public void onClickedUpdate(ActionEvent actionEvent) {
        validateInput();
        if(tfAlbum.getText().equals(selectedSong.getAlbum()) && tfTitle.getText().equals(selectedSong.getName())
                &&tfArtist.getText().equals(selectedSong.getAlbum())){  //如果没有修改，直接关闭
            tfArtist.getScene().getWindow().hide();

        }else { //否则，启动服务更新
            UpdateSongService updateSongService = applicationContext.getBean(UpdateSongService.class);
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
        btnCancel1.getScene().getWindow().hide();
    }
}
