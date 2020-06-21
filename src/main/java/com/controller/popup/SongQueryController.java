package com.controller.popup;

import com.config.Category;
import com.controller.content.TabSongController;
import com.service.QueryByNameLikeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @author super lollipop
 * @date 20-2-22
 */
public class SongQueryController {

    @FXML
    private TextField tfName;

    @FXML
    private Button btnCancel;

    private TabSongController tabSongController;

    public TextField getTfName() {
        return tfName;
    }

    public void setTabSongController(TabSongController tabSongController) {
        this.tabSongController = tabSongController;
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    public void onClickedConfirm(ActionEvent actionEvent) {
        if (!tfName.getText().trim().equals("")){   //如果输入的内容不为空
            QueryByNameLikeService queryByNameLikeService = new QueryByNameLikeService();
            queryByNameLikeService.setName(tfName.getText().trim());
            queryByNameLikeService.setCategory(Category.Song);
            tabSongController.getProgressIndicator().visibleProperty().bind(queryByNameLikeService.runningProperty());
            queryByNameLikeService.setOnSucceeded(event -> {
                if (tabSongController.getTableViewSong().itemsProperty().isBound()){
                    tabSongController.getTableViewSong().itemsProperty().unbind();
                }
                tabSongController.getTableViewSong().setItems(queryByNameLikeService.getValue());
            });
            queryByNameLikeService.start();
        }
        onClickedCancel(actionEvent);
    }
}
