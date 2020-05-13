package com.controller.popup;

import com.component.AutoCompleteTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("prototype")
public class AlbumInsertController {


    @FXML
    private BorderPane root;

    @FXML
    private Button btnCancel;


    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize(){
        AutoCompleteTextField autoCompleteTextField = new AutoCompleteTextField();
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("456");
        list.add("789");
        autoCompleteTextField.setEntries(list);
        root.setBottom(autoCompleteTextField);
    }

    @FXML
    public void onClickedChooseImageFile(MouseEvent mouseEvent) {
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    @FXML
    public void onClickedConfirm(ActionEvent actionEvent) {
    }
}
