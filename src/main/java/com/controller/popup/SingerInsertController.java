package com.controller.popup;

import com.service.InsertSingerService;
import com.util.AlertUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author super lollipop
 * @date 20-2-24
 */
@Controller
public class SingerInsertController {

    @FXML
    private ImageView ivImage;

    @FXML
    private TextField tfWeight;

    @FXML
    private TextField tfHeight;

    @FXML
    private TextField tfName;

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnCancel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private DatePicker dpBirthday;

    @FXML
    private TextArea taDescription;

    @FXML
    private ComboBox<String> cbConstellation;

    private File imageFile;

    private byte[] imageBytes;

    public ImageView getIvImage() {
        return ivImage;
    }

    public TextField getTfWeight() {
        return tfWeight;
    }

    public TextField getTfHeight() {
        return tfHeight;
    }

    public TextField getTfName() {
        return tfName;
    }

    public Button getBtnUpload() {
        return btnUpload;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public DatePicker getDpBirthday() {
        return dpBirthday;
    }

    public TextArea getTaDescription() {
        return taDescription;
    }

    public ComboBox<String> getCbConstellation() {
        return cbConstellation;
    }

    public File getImageFile() {
        return imageFile;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    @Autowired
    private ApplicationContext applicationContext;

    public void initialize(){
        Platform.runLater(()->{
            tfName.requestFocus();
        });
        tfHeight.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.isDeleted() || change.getText().matches("^[0-9.]")){
                return change;
            }else {
                return null;
            }
        }));
        tfWeight.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.isDeleted() || change.getText().matches("^[0-9.]")){
                return change;
            }else {
                return null;
            }
        }));
    }

    @FXML
    void onClickedCancel(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void onClickedChooseImageFile(MouseEvent event) throws IOException {
        FileChooser imageFileChooser = new FileChooser();
        imageFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        imageFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG图片(*.jpg)","*.jpg"),
                new FileChooser.ExtensionFilter("PNG图片(*.png)","*.png")
        );
        imageFile = imageFileChooser.showOpenDialog(ivImage.getScene().getWindow());
        if (imageFile != null){
            if (imageFile.length() / 1024 /1024 > 1){ //文件大小大于1m，不允许选择
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示信息");
                alert.setContentText("图片文件大于1M");
                alert.showAndWait();
            }else {
                Image image = new Image("file:" + imageFile.toPath().toString(),150,150,true,true);
                ivImage.setImage(image);
                imageBytes = Files.readAllBytes(imageFile.toPath());
            }
        }
    }

    @FXML
    void onClickedConfirm(ActionEvent event) {
        if (validateInput()){
            InsertSingerService insertSingerService = applicationContext.getBean(InsertSingerService.class);
            progressIndicator.visibleProperty().bind(insertSingerService.runningProperty());
            insertSingerService.restart();
        }
    }



    private boolean validateInput(){
        if (tfName.getText().trim().equals("") || imageFile == null ){
            AlertUtils.showWarning("信息输入不完整");
            return false;
        } else {
            return true;
        }
    }
}
