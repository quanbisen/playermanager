package com.controller.popup;

import com.config.Category;
import com.pojo.Album;
import com.service.QueryByNameLikeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AlbumQueryController {

    @FXML
    private Button btnCancel;

    @FXML
    private TextField tfName;

    private ProgressIndicator progressIndicator;

    private TableView<Album> tableViewAlbum;

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public void setTableViewAlbum(TableView<Album> tableViewAlbum) {
        this.tableViewAlbum = tableViewAlbum;
    }

    @FXML
    public void onClickedConfirm(ActionEvent actionEvent) {
        if (!tfName.getText().trim().equals("")){   //输入文本不为空，执行操作
            QueryByNameLikeService queryByNameLikeService = new QueryByNameLikeService();
            queryByNameLikeService.setCategory(Category.Album);
            queryByNameLikeService.setName(tfName.getText());
            progressIndicator.visibleProperty().bind(queryByNameLikeService.runningProperty());
            onClickedCancel(actionEvent);       //触发“取消”按钮时间，关闭stage界面
            queryByNameLikeService.setOnSucceeded(event -> {
                tableViewAlbum.setItems(queryByNameLikeService.getValue());
            });
            queryByNameLikeService.start();
        }
        onClickedCancel(actionEvent);
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }
}
