package com.controller.content;

import com.application.SpringFXMLLoader;
import com.config.ServerConfig;
import com.controller.popup.AlbumQueryController;
import com.pojo.Album;
import com.config.Category;
import com.service.QueryAllService;
import com.util.HttpClientUtils;
import com.util.StageUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Scope("prototype")
public class TabAlbumController {

    @FXML
    public StackPane root;

    @FXML
    private ProgressIndicator progressIndicator;

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
    private TableView<Album> tableViewAlbum;

    @FXML
    private TableColumn<Album, Integer> columnID;

    @FXML
    private TableColumn<Album, String> columnAlbum;

    @FXML
    private TableColumn<Album, String> columnSinger;

    @FXML
    private TableColumn<Album, Date> columnPublishTime;

    @FXML
    private TableColumn<Album, String> columnDescription;

    @Resource
    private ApplicationContext applicationContext;

    public void initialize(){
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSinger.setCellValueFactory(new PropertyValueFactory<>("singerName"));
        columnPublishTime.setCellValueFactory(new PropertyValueFactory<>("publishTime"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPublishTime.setCellFactory(c -> {
            TableCell<Album, Date> cell = new TableCell<Album, Date>() {
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

    private void updateTable(){
        QueryAllService queryAllService = applicationContext.getBean(QueryAllService.class);
        queryAllService.setCategoryEnum(Category.Album);
        queryAllService.start();
        progressIndicator.visibleProperty().bind(queryAllService.runningProperty());
        queryAllService.setOnSucceeded(event -> {
            ObservableList observableList = queryAllService.getValue();
            tableViewAlbum.setItems((ObservableList<Album>) observableList);
        });
    }

    @FXML
    public void onClickedAdd(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/popup/album-insert.fxml").load());
            StageUtils.synchronizeCenter((Stage) btnAdd.getScene().getWindow(),stage);
            stage.show();
        }
    }

    @FXML
    public void onClickedModify(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            Album album = tableViewAlbum.getSelectionModel().getSelectedItem();
            if (album == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setContentText("还没有选中歌曲");
                alert.showAndWait();
            }else {
                Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/popup/song-update.fxml").load());
                StageUtils.synchronizeCenter((Stage) btnAdd.getScene().getWindow(),stage);
                stage.show();
            }

        }
    }

    /**“删除”按钮的事件*/
    @FXML
    public void onClickedDelete(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            Album album = tableViewAlbum.getSelectionModel().getSelectedItem();
            if (album == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setContentText("还没有选中歌曲");
                alert.showAndWait();
            }else {
                String string = HttpClientUtils.executeDelete(applicationContext.getBean(ServerConfig.class).getSongURL() + "/delete/" + album.getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                if (string.equals("1")){    //如果返回等于一
                    alert.setContentText("删除成功");
                    tableViewAlbum.getItems().remove(album);
                }else {
                    alert.setContentText("删除失败");
                }
                alert.showAndWait();
            }
        }
    }



    @FXML
    public void onClickedQuery(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            Stage primaryStage = (Stage) btnQuery.getScene().getWindow();
            FXMLLoader fxmlLoader = applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/popup/album-query.fxml");
            Stage stage = StageUtils.getStage(primaryStage,fxmlLoader.load());
            StageUtils.synchronizeCenter(primaryStage,stage);
            AlbumQueryController albumQueryController = fxmlLoader.getController();
            albumQueryController.setProgressIndicator(progressIndicator);
            albumQueryController.setTableViewAlbum(tableViewAlbum);
            stage.show();
        }
    }

    @FXML
    public void onClickedRefresh(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            updateTable();
        }
    }
}
