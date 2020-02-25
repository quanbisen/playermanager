package com.controller.popup;

import com.controller.tabcontent.TabSongController;
import com.service.QuerySongByNameService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

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

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private TabSongController tabSongController;

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
        QuerySongByNameService querySongByNameService = applicationContext.getBean(QuerySongByNameService.class);
        tabSongController.getProgressIndicator().visibleProperty().bind(querySongByNameService.runningProperty());
        tabSongController.getTableViewSong().itemsProperty().bind(querySongByNameService.valueProperty());
        querySongByNameService.start();
    }
}
