package com.controller.content;

import com.config.Category;
import com.config.ServerConfig;
import com.controller.popup.SingerInsertController;
import com.controller.popup.SingerQueryController;
import com.controller.popup.SingerUpdateController;
import com.pojo.Singer;
import com.service.DeleteByIDService;
import com.service.QueryAllService;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import com.util.StageUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-2-24
 */

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
            TableCell<Singer, Date> cell = new TableCell<>() {
                private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        if(item != null)
                            this.setText(simpleDateFormat.format(item));
                    }
                }
            };
            return cell;
        });

        updateTable();
    }

    /**请求内容更新表格*/
    public void updateTable(){
        QueryAllService queryAllService = new QueryAllService();
        queryAllService.setCategory(Category.Singer);
        queryAllService.setOnSucceeded(event -> {
            tableViewSinger.setItems(queryAllService.getValue());
        });
        progressIndicator.visibleProperty().bind(queryAllService.runningProperty());
        queryAllService.start();
    }

    @FXML
    void onClickedAdd(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popup/singer-insert.fxml"));
            Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),fxmlLoader.load());
            SingerInsertController singerInsertController = fxmlLoader.getController();
            singerInsertController.setTabSingerController(this);
            StageUtils.synchronizeCenter((Stage) btnAdd.getScene().getWindow(),stage);
            stage.show();
        }
    }

    @FXML
    void onClickedModify(MouseEvent event) throws IOException, InterruptedException {
        Singer singer = tableViewSinger.getSelectionModel().getSelectedItem();
        if (singer == null){
            AlertUtils.showError("还没有选中歌手");
        }else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popup/singer-update.fxml"));
            Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),fxmlLoader.load());
            SingerUpdateController singerUpdateController = fxmlLoader.getController();
            singerUpdateController.setTabSingerController(this);
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
            DeleteByIDService deleteByIDService = new DeleteByIDService();
            deleteByIDService.setId(singer.getId());
            deleteByIDService.setCategory(Category.Singer);
            progressIndicator.visibleProperty().bind(deleteByIDService.runningProperty());
            deleteByIDService.setOnSucceeded(event1 -> {
                if (deleteByIDService.getValue().equals("1")){
                    AlertUtils.showInformation("删除成功");
                    tableViewSinger.getItems().remove(singer);
                }else {
                    AlertUtils.showError("删除失败");
                }
            });
            deleteByIDService.start();  //启动服务
        }
    }


    @FXML
    void onClickedQuery(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popup/singer-query.fxml"));
        Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),fxmlLoader.load());
        SingerQueryController singerQueryController = fxmlLoader.getController();
        singerQueryController.setTabSingerController(this);
        StageUtils.synchronizeCenter((Stage) btnAdd.getScene().getWindow(),stage);
        stage.show();
    }

    @FXML
    void onClickedRefresh(MouseEvent event) {
        updateTable();
    }

}
