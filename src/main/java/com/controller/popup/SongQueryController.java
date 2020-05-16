package com.controller.popup;

import com.config.Category;
import com.controller.content.TabSongController;
import com.service.QueryByNameLikeService;
import com.service.QuerySongByNameService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Controller
public class SongQueryController {

    @FXML
    private TextField tfName;

    @FXML
    private Button btnCancel;

    private ApplicationContext applicationContext;

    private TabSongController tabSongController;

    @Autowired
    public void constructor(ApplicationContext applicationContext,TabSongController tabSongController){
        this.applicationContext = applicationContext;
        this.tabSongController = tabSongController;
    }

    public TextField getTfName() {
        return tfName;
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    public void onClickedConfirm(ActionEvent actionEvent) {
        onClickedCancel(actionEvent);
        if (!tfName.getText().trim().equals("")){
            QueryByNameLikeService queryByNameLikeService = applicationContext.getBean(QueryByNameLikeService.class);
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
    }
}
