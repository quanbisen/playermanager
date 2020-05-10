package com.controller.content;

import com.application.SpringFXMLLoader;
import com.config.ServerConfig;
import com.pojo.Singer;
import com.service.QueryAllSingerService;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import com.util.StageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-2-24
 */
@Controller
public class TabSingerController {
    @FXML
    private StackPane root;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnModify;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnQuery;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableView<Singer> tableViewSinger;

    @FXML
    private TableColumn<Singer, Integer> columnID;

    @FXML
    private TableColumn<Singer, String> columnName;

    @FXML
    private TableColumn<Singer, Date> columnBirthday;

    @FXML
    private TableColumn<Singer, Float> columnHeight;

    @FXML
    private TableColumn<Singer, Float> columnWeight;

    @FXML
    private TableColumn<Singer, String> columnConstellation;

    @FXML
    private TableColumn<Singer, String> columnDescription;

    @FXML
    private ProgressIndicator progressIndicator;

    @Autowired
    private ApplicationContext applicationContext;

    public TableView<Singer> getTableViewSinger() {
        return tableViewSinger;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public void initialize(){
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnBirthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        columnHeight.setCellValueFactory(new PropertyValueFactory<>("height"));
        columnWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        columnConstellation.setCellValueFactory(new PropertyValueFactory<>("constellation"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnBirthday.setCellFactory(c -> {
            TableCell<Singer, Date> cell = new TableCell<Singer, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        if(item != null)
                            this.setText(format.format(item));
                    }
                }
            };
            return cell;
        });

        updateTable();
    }

    /**请求内容更新表格*/
    public void updateTable(){
        QueryAllSingerService queryAllSingerService = applicationContext.getBean(QueryAllSingerService.class);
        tableViewSinger.itemsProperty().bind(queryAllSingerService.valueProperty());
        progressIndicator.visibleProperty().bind(queryAllSingerService.runningProperty());
        queryAllSingerService.start();
    }

    @FXML
    void onClickedAdd(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/popup/singer-insert.fxml").load());
            StageUtils.synchronizeCenter((Stage) btnAdd.getScene().getWindow(),stage);
            stage.show();
        }
    }

    @FXML
    void onClickedModify(MouseEvent event) throws IOException {
        Singer singer = tableViewSinger.getSelectionModel().getSelectedItem();
        if (singer == null){
            AlertUtils.showError("还没有选中歌手");
        }else {
            Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/popup/singer-update.fxml").load());
            StageUtils.synchronizeCenter((Stage) btnAdd.getScene().getWindow(),stage);
            stage.show();
        }
    }


    @FXML
    void onClickedDelete(MouseEvent event) throws IOException {
        Singer singer = tableViewSinger.getSelectionModel().getSelectedItem();
        if (singer == null){
            AlertUtils.showError("还没有选中歌手");
        }else {
            String string = HttpClientUtils.executeDelete(applicationContext.getBean(ServerConfig.class).getSingerURL() + "/delete/" + singer.getId());
            if (string.equals("1")){    //如果返回等于一
                AlertUtils.showInformation("删除成功");
                tableViewSinger.getItems().remove(singer);
            }else {
                AlertUtils.showError("删除失败");
            }
        }
    }


    @FXML
    void onClickedQuery(MouseEvent event) throws IOException {
        Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/popup/singer-query.fxml").load());
        StageUtils.synchronizeCenter((Stage) btnAdd.getScene().getWindow(),stage);
        stage.show();
    }

    @FXML
    void onClickedRefresh(MouseEvent event) {
        updateTable();
    }

}
