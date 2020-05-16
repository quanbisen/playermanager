package com.controller.popup;

import com.config.Category;
import com.pojo.Album;
import com.pojo.Singer;
import com.service.InsertAlbumService;
import com.service.QueryByNameLikeService;
import com.util.AlertUtils;
import com.util.StageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.IOException;

@Controller
@Scope("prototype")
public class AlbumInsertController {

    @FXML
    private ImageView ivImage;

    @FXML
    private TextField tfName;

    @FXML
    private Button btnCancel;

    @FXML
    private TextArea taDescription;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextField tfSinger;

    @FXML
    private TableView<Singer> tableViewSinger;

    @FXML
    private TableColumn<Singer,Integer> columnID;

    @FXML
    private TableColumn<Singer,String> columnSinger;

    private ApplicationContext applicationContext;

    private File imageFile;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        tfSinger.textProperty().addListener((observable, oldValue, newValue) -> {tfSinger.setUserData(null);}); //设置为空，限制在tableView上设置值
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnSinger.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    public void onClickedChooseImageFile(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            imageFile = StageUtils.getImageFileChooser().showOpenDialog(ivImage.getScene().getWindow());
            if (imageFile != null){
                if (imageFile.length() / 1024 /1024 > 1){ //文件大小大于1m，不允许选择
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("提示信息");
                    alert.setContentText("图片文件大于1M");
                    alert.showAndWait();
                }else {
                    Image image = new Image("file:" + imageFile.toPath().toString(),150,150,true,true);
                    ivImage.setImage(image);
                }
            }
        }
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    public void onClickedConfirm(ActionEvent actionEvent) {
        if (!validateInput()){
            AlertUtils.showWarning("信息输入不完整");
        }else {
            InsertAlbumService insertAlbumService = applicationContext.getBean(InsertAlbumService.class);
            Album album = new Album();
            album.setName(tfName.getText().trim());
            if (!taDescription.getText().trim().equals("")){
                album.setDescription(taDescription.getText().trim());
            }
            album.setSingerID(((Singer)tfSinger.getUserData()).getId());
            insertAlbumService.setAlbum(album);
            insertAlbumService.setImageFile(imageFile);
            insertAlbumService.setBtnCancel(btnCancel);
            insertAlbumService.start();
        }
    }

    private boolean validateInput() {
        if (StringUtils.isEmpty(tfName.getText().trim()) || imageFile == null || imageFile.length() == 0){
            return false;
        }else{
            if (tfSinger.getUserData() == null){
                return false;
            }else {
                return true;
            }
        }
    }

    @FXML
    public void onClickedQuery(ActionEvent actionEvent) {
        if (!tfSinger.getText().trim().equals("")){
            QueryByNameLikeService queryByNameLikeService = applicationContext.getBean(QueryByNameLikeService.class);
            queryByNameLikeService.setCategory(Category.Singer);
            queryByNameLikeService.setName(tfSinger.getText());
            progressIndicator.visibleProperty().bind(queryByNameLikeService.runningProperty());
            queryByNameLikeService.setOnSucceeded(event -> tableViewSinger.setItems(queryByNameLikeService.getValue()));
            queryByNameLikeService.start();
        }
    }

    @FXML
    public void onClickedSelect(MouseEvent mouseEvent) {
        if (tableViewSinger.getItems() != null && tableViewSinger.getItems().size() > 0){
            Singer singer = tableViewSinger.getSelectionModel().getSelectedItem();
            if (singer != null){
                tfSinger.setText(singer.getName());
                tfSinger.setUserData(singer);
            }
        }
    }
}
