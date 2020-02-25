package com.controller.popup;

import com.controller.tabcontent.TabSingerController;
import com.service.QuerySingerByNameService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author super lollipop
 * @date 20-2-25
 */
@Controller
public class SingerQueryController {


    @FXML
    private Button btnCancel;

    @FXML
    private TextField tfName;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private TabSingerController tabSingerController;

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
        QuerySingerByNameService querySingerByNameService = applicationContext.getBean(QuerySingerByNameService.class);
        tabSingerController.getProgressIndicator().visibleProperty().bind(querySingerByNameService.runningProperty());
        tabSingerController.getTableViewSinger().itemsProperty().bind(querySingerByNameService.valueProperty());
        querySingerByNameService.start();
    }
}
