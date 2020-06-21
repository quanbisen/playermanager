package com.controller.popup;

import com.config.Category;
import com.controller.content.TabSingerController;
import com.pojo.Singer;
import com.service.QueryByNameLikeService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author super lollipop
 * @date 20-2-25
 */

public class SingerQueryController {

    @FXML
    private Button btnCancel;

    @FXML
    private TextField tfName;

    private TabSingerController tabSingerController;

    public void setTabSingerController(TabSingerController tabSingerController) {
        this.tabSingerController = tabSingerController;
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    @FXML
    public void onClickedConfirm(ActionEvent actionEvent) {
        if (!tfName.getText().trim().equals("")){
            QueryByNameLikeService queryByNameLikeService = new QueryByNameLikeService();
            queryByNameLikeService.setName(tfName.getText());
            queryByNameLikeService.setCategory(Category.Singer);
            tabSingerController.getProgressIndicator().visibleProperty().bind(queryByNameLikeService.runningProperty());
            queryByNameLikeService.setOnSucceeded(event -> {
                ObservableList<Singer> observableList = queryByNameLikeService.getValue();
                TableView<Singer> tableViewSinger = tabSingerController.getTableViewSinger();
                if (tableViewSinger.itemsProperty().isBound()){
                    tableViewSinger.itemsProperty().unbind();
                }
                tabSingerController.getTableViewSinger().setItems(observableList);
            });
            queryByNameLikeService.start();
        }
        onClickedCancel(actionEvent);
    }
}
