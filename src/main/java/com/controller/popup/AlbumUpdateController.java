package com.controller.popup;

import com.controller.content.TabAlbumController;
import com.pojo.Album;
import com.service.LoadOnlineImageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
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

    private TabAlbumController tabAlbumController;

    private ApplicationContext applicationContext;

    @Autowired
    public void constructor(TabAlbumController tabAlbumController,ApplicationContext applicationContext){
        this.tabAlbumController = tabAlbumController;
        this.applicationContext = applicationContext;
    }

    public void initialize(){
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
            LoadOnlineImageService loadOnlineImageService = applicationContext.getBean(LoadOnlineImageService.class);
            loadOnlineImageService.setOptimizeURI(album.getImageURL());
            loadOnlineImageService.setImageSize(150,150);
            loadOnlineImageService.setOnSucceeded(event -> ivAlbum.setImage(loadOnlineImageService.getValue()));
            loadOnlineImageService.start();
        }
    }

    @FXML
    void onClickedAlbumManagement(ActionEvent event) {

    }

    @FXML
    void onClickedCancel(ActionEvent event) {
        ((Stage)ivAlbum.getScene().getWindow()).close();
        destroy();
    }

    @FXML
    void onClickedUpdate(ActionEvent event) {

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
