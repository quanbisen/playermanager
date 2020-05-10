package com.controller.popup;

import com.controller.content.TabSingerController;
import com.pojo.Singer;
import com.service.UpdateSingerService;
import com.util.AlertUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import java.time.ZoneId;

/**
 * @author super lollipop
 * @date 20-2-25
 */
@Controller
public class SingerUpdateController {


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
    private ImageView ivImage;

    @FXML
    private ComboBox<String> cbConstellation;

    private Singer selectedSinger;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TabSingerController tabSingerController;

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

    public Singer getSelectedSinger() {
        return selectedSinger;
    }

    public void initialize(){
        Platform.runLater(()->{
            tfName.requestFocus();
        });
        selectedSinger = tabSingerController.getTableViewSinger().getSelectionModel().getSelectedItem();
        tfName.setText(selectedSinger.getName());
        if (selectedSinger.getBirthday() != null){
            dpBirthday.setValue(selectedSinger.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (selectedSinger.getHeight() == 0){
            tfHeight.setText("");
        }else {
            tfHeight.setText(String.valueOf(selectedSinger.getHeight()));
        }
        if (selectedSinger.getWeight() == 0){
            tfWeight.setText("");
        }else {
            tfWeight.setText(String.valueOf(selectedSinger.getWeight()));
        }
        cbConstellation.setValue(selectedSinger.getConstellation());
        taDescription.setText(selectedSinger.getDescription());
        Image image = new Image(selectedSinger.getImageURL(),150,150,true,true);
        if (!image.isError()){
            ivImage.setImage(image);
        }
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
    void onClickedConfirm(ActionEvent event) {
        if (validateInput()){
            UpdateSingerService updateSingerService = applicationContext.getBean(UpdateSingerService.class);
            progressIndicator.visibleProperty().bind(updateSingerService.runningProperty());
            updateSingerService.restart();
        }
    }

    private boolean validateInput(){
        if (tfName.getText().trim().equals("")){
            AlertUtils.showWarning("信息输入不完整");
            return false;
        } else {
            return true;
        }
    }
}
