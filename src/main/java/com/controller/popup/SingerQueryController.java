package com.controller.popup;

import com.config.Category;
import com.controller.content.TabSingerController;
import com.pojo.Singer;
import com.service.QueryByNameService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author super lollipop
 * @date 20-2-25
 */
@Controller
@Scope("prototype")
public class SingerQueryController {

    @FXML
    private Button btnCancel;

    @FXML
    private TextField tfName;

    private ApplicationContext applicationContext;

    private TabSingerController tabSingerController;

    @Autowired
    public void constructor(ApplicationContext applicationContext,TabSingerController tabSingerController){
        this.applicationContext = applicationContext;
        this.tabSingerController = tabSingerController;
    }

    @FXML
    public void onClickedCancel(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    @FXML
    public void onClickedConfirm(ActionEvent actionEvent) {
        if (!tfName.getText().trim().equals("")){
            QueryByNameService queryByNameService = applicationContext.getBean(QueryByNameService.class);
            tabSingerController.getProgressIndicator().visibleProperty().bind(queryByNameService.runningProperty());
            queryByNameService.setCategory(Category.Singer);
            queryByNameService.setName(tfName.getText());
            queryByNameService.setOnSucceeded(event -> {
                ObservableList<Singer> observableList = queryByNameService.getValue();
                TableView<Singer> tableViewSinger = tabSingerController.getTableViewSinger();
                if (tableViewSinger.itemsProperty().isBound()){
                    tableViewSinger.itemsProperty().unbind();
                }
                tabSingerController.getTableViewSinger().setItems(observableList);
            });
            queryByNameService.start();
        }
        onClickedCancel(actionEvent);
    }
}
