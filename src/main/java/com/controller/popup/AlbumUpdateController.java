package com.controller.popup;

import com.controller.content.TabAlbumController;
import com.pojo.Album;
import com.service.LoadOnlineImageService;
import com.service.UpdateAlbumService;
import com.util.StageUtils;
import com.util.StringUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AlbumUpdateController {

    @FXML
    private ImageView ivAlbum;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfSinger;

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnCancel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextArea taDescription;

    @FXML
    private DatePicker dpPublishTime;

    private File imageFile;

    private TabAlbumController tabAlbumController;

    public TextArea getTaDescription() {
        return taDescription;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setTabAlbumController(TabAlbumController tabAlbumController) {
        this.tabAlbumController = tabAlbumController;
    }

    public void initialize(){
        Platform.runLater(()->{
            /**原有数据初始化显示*/
            Album album = tabAlbumController.getTableViewAlbum().getSelectionModel().getSelectedItem();
            if (!StringUtils.isEmpty(album.getName())){
                tfName.setText(album.getName());
            }
            if (!StringUtils.isEmpty(album.getSingerName())){
                tfSinger.setText(album.getSingerName());
            }
            if (album.getPublishTime() != null){
                dpPublishTime.setValue(LocalDateTime.ofInstant(album.getPublishTime().toInstant(), ZoneId.systemDefault()).toLocalDate());
            }
            if (!StringUtils.isEmpty(album.getDescription())){
                taDescription.setText(album.getDescription());
            }
            if (!StringUtils.isEmpty(album.getImageURL())){
                LoadOnlineImageService loadOnlineImageService = new LoadOnlineImageService();
                loadOnlineImageService.setPreferURI(album.getImageURL());
                loadOnlineImageService.setImageSize(150,150);
                loadOnlineImageService.setOnSucceeded(event -> ivAlbum.setImage(loadOnlineImageService.getValue()));
                loadOnlineImageService.start();
            }
        });
    }

    @FXML
    void onClickedAlbumManagement(ActionEvent event) {

    }

    @FXML
    public void onClickedAlbumImage(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            imageFile = StageUtils.getImageFileChooser().showOpenDialog(ivAlbum.getScene().getWindow());
            if (imageFile != null){
                if (imageFile.length() / 1024 /1024 > 1){ //文件大小大于1m，不允许选择
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("提示信息");
                    alert.setContentText("图片文件大于1M");
                    alert.showAndWait();
                }else {
                    Image image = new Image("file:" + imageFile.toPath().toString(),150,150,true,true);
                    ivAlbum.setImage(image);
                }
            }
        }
    }

    @FXML
    void onClickedCancel(ActionEvent event) {
        ((Stage)ivAlbum.getScene().getWindow()).close();
        destroy();
    }

    @FXML
    void onClickedUpdate(ActionEvent event) {
        UpdateAlbumService updateAlbumService = new UpdateAlbumService();
        updateAlbumService.setTabAlbumController(tabAlbumController);
        updateAlbumService.setAlbumUpdateController(this);
        progressIndicator.visibleProperty().bind(updateAlbumService.runningProperty());
        updateAlbumService.start();
    }

    public void destroy(){
        tfName = null;
        ivAlbum = null;
        tfName = null;
        btnUpload = null;
        btnCancel = null;
        progressIndicator = null;
        taDescription = null;
        dpPublishTime = null;
    }


}
