package com.controller.tabcontent;

import com.application.SpringFXMLLoader;
import com.config.ServerConfig;
import com.pojo.Song;
import com.service.QueryAllSongService;
import com.util.HttpClientUtils;
import com.util.StageUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Controller
public class TabSongController {

    private static Logger logger;

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
    private TableView<Song> tableViewSong;

    @FXML
    private TableColumn<Song, Integer> columnID;

    @FXML
    private TableColumn<Song, String> columnName;

    @FXML
    private TableColumn<Song, String> columnSinger;

    @FXML
    private TableColumn<Song, String> columnAlbum;

    @FXML
    private TableColumn<Song, String> columnTotalTime;

    @FXML
    private TableColumn<Song, String> columnSize;

    @Autowired
    private ApplicationContext applicationContext;

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public TableView<Song> getTableViewSong() {
        return tableViewSong;
    }

    public void initialize() throws IOException {
        Platform.runLater(()->{
            root.prefWidthProperty().bind(((Region)(root.getParent())).widthProperty());
        });

        root.prefWidthProperty().addListener(((observable, oldValue, newValue) -> {
            columnID.setPrefWidth(observable.getValue().doubleValue() / 6 * 0.8);
            columnName.setPrefWidth(observable.getValue().doubleValue() / 6 * 1.5);
            columnSinger.setPrefWidth(observable.getValue().doubleValue() / 6 * 1);
            columnAlbum.setPrefWidth(observable.getValue().doubleValue() / 6 * 1);
            columnTotalTime.setPrefWidth(observable.getValue().doubleValue() / 6 * 1);
            columnSize.setPrefWidth(observable.getValue().doubleValue() / 6 * 0.7);
        }));

        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSinger.setCellValueFactory(new PropertyValueFactory<>("singer"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        columnTotalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        columnSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        updateTable();
    }

    public void updateTable(){
        QueryAllSongService queryAllSongService = applicationContext.getBean(QueryAllSongService.class);
        progressIndicator.visibleProperty().bind(queryAllSongService.runningProperty());
        tableViewSong.itemsProperty().bind(queryAllSongService.valueProperty());
        queryAllSongService.start();
    }

    @FXML
    public void onClickedAdd(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            Stage stage = StageUtils.getStage((Stage) btnAdd.getScene().getWindow(),applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/popup/song-insert.fxml").load());
            StageUtils.synchronizeCenter((Stage) btnAdd.getScene().getWindow(),stage);
            stage.show();
        }
    }

    @FXML
    public void onClickedModify(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            Song song = tableViewSong.getSelectionModel().getSelectedItem();
            if (song == null){
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
            Song song = tableViewSong.getSelectionModel().getSelectedItem();
            if (song == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setContentText("还没有选中歌曲");
                alert.showAndWait();
            }else {
                String string = HttpClientUtils.executeDelete(applicationContext.getBean(ServerConfig.class).getSongURL() + "/delete/" + song.getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                if (string.equals("1")){    //如果返回等于一
                    alert.setContentText("删除成功");
                    tableViewSong.getItems().remove(song);
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
            Stage stage = StageUtils.getStage(primaryStage,applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/popup/song-query.fxml").load());
            StageUtils.synchronizeCenter(primaryStage,stage);
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