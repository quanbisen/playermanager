package com.controller.content;

import com.controller.popup.AlbumInsertController;
import com.controller.popup.AlbumQueryController;
import com.controller.popup.AlbumUpdateController;
import com.pojo.Album;
import com.config.Category;
import com.service.DeleteByIDService;
import com.service.QueryAllService;
import com.util.AlertUtils;
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

public class TabAlbumController {

    @FXML
    public StackPane root;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnQuery;

    @FXML
    private TableView<Album> tableViewAlbum;

    @FXML
    private TableColumn<Album, Integer> columnID;

    @FXML
    private TableColumn<Album, String> columnAlbum;

    @FXML
    private TableColumn<Album, String> columnSinger;

    @FXML
    private TableColumn<Album,Integer> columnSongCount;

    @FXML
    private TableColumn<Album, Date> columnPublishTime;

    @FXML
    private TableColumn<Album, String> columnDescription;

    public TableView<Album> getTableViewAlbum() {
        return tableViewAlbum;
    }

    public void initialize(){
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSinger.setCellValueFactory(new PropertyValueFactory<>("singerName"));
        columnSongCount.setCellValueFactory(new PropertyValueFactory<>("songCount"));
        columnPublishTime.setCellValueFactory(new PropertyValueFactory<>("publishTime"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPublishTime.setCellFactory(c -> {
            TableCell<Album, Date> cell = new TableCell<>() {
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

    public void updateTable(){
        QueryAllService queryAllService = new QueryAllService();
        queryAllService.setCategory(Category.Album);
        queryAllService.start();
        progressIndicator.visibleProperty().bind(queryAllService.runningProperty());
        queryAllService.setOnSucceeded(event -> {
            tableViewAlbum.setItems(queryAllService.getValue());
            tableViewAlbum.getSelectionModel().clearSelection();
        });
    }

    @FXML
    public void onClickedAdd(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popup/album-insert.fxml"));
            Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),fxmlLoader.load());
            AlbumInsertController albumInsertController = fxmlLoader.getController();
            albumInsertController.setTabAlbumController(this);
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
                alert.setContentText("还没有选中专辑");
                alert.showAndWait();
            }else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popup/album-update.fxml"));
                Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),fxmlLoader.load());
                AlbumUpdateController albumUpdateController = fxmlLoader.getController();
                albumUpdateController.setTabAlbumController(this);
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
                DeleteByIDService deleteByIDService = new DeleteByIDService();
                deleteByIDService.setId(album.getId());
                deleteByIDService.setCategory(Category.Album);
                progressIndicator.visibleProperty().bind(deleteByIDService.runningProperty());
                deleteByIDService.setOnSucceeded(workerStateEvent -> {
                    if (deleteByIDService.getValue().equals("success")){
                        updateTable();
                        AlertUtils.showInformation("删除成功");
                    }else {
                        AlertUtils.showError("删除失败");
                    }
                });
                deleteByIDService.start();
            }
        }
    }



    @FXML
    public void onClickedQuery(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/popup/album-query.fxml"));
            Stage primaryStage = (Stage) btnQuery.getScene().getWindow();
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
