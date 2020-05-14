package com.controller.popup;

import com.config.Category;
import com.pojo.Album;
import com.service.QueryByNameService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class AlbumQueryController {

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField tfName;

    private ProgressIndicator progressIndicator;

    private TableView<Album> tableViewAlbum;

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public void setTableViewAlbum(TableView<Album> tableViewAlbum) {
        this.tableViewAlbum = tableViewAlbum;
    }

    @FXML
    public void onClickedConfirm(ActionEvent actionEvent) {
        QueryByNameService queryByNameService = applicationContext.getBean(QueryByNameService.class);
        queryByNameService.setCategory(Category.Album);
        queryByNameService.setName(tfName.getText());
        progressIndicator.visibleProperty().bind(queryByNameService.runningProperty());
        onClickedCancel(actionEvent);
        queryByNameService.setOnSucceeded(event -> {
            tableViewAlbum.setItems(queryByNameService.getValue());
        });
        queryByNameService.start();
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }
}
