package com.controller.content;

import com.application.SpringFXMLLoader;
import com.config.Category;
import com.pojo.Album;
import com.pojo.Singer;
import com.pojo.Song;
import com.service.DeleteByIDService;
import com.service.QueryAllService;
import com.util.AlertUtils;
import com.util.StageUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private TableColumn<Song, List<Singer>> columnSinger;

    @FXML
    private TableColumn<Song, String> columnAlbum;

    @FXML
    private TableColumn<Song, String> columnTotalTime;

    @FXML
    private TableColumn<Song, String> columnSize;

    @FXML
    private TableColumn<Song, Date> columnPublishTime;

    @FXML
    private TableColumn<Song,Album> columnCollectAlbum;

    @FXML
    private TableColumn<Song,Date> columnCollectTime;

    private ApplicationContext applicationContext;

    @Autowired
    public void constructor(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

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
        columnSinger.setCellValueFactory(param -> {
            StringBuilder singer = new StringBuilder();
            for (int i = 0; i < param.getValue().getSingerList().size(); i++) {
                singer.append(param.getValue().getSingerList().get(i).getName());
                if (i != param.getValue().getSingerList().size() -1){
                    singer.append("/");
                }
            }
            SimpleObjectProperty simpleObjectProperty = new SimpleObjectProperty(singer.toString());
            return simpleObjectProperty;
        });
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("albumName"));
        columnTotalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        columnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        columnPublishTime.setCellValueFactory(new PropertyValueFactory<>("publishTime"));
        formatDateColumn(columnPublishTime);
        columnCollectAlbum.setCellValueFactory(param -> {
            if (param.getValue().getAlbumObject() != null){
                SimpleObjectProperty simpleObjectProperty = new SimpleObjectProperty(param.getValue().getAlbumObject().getName());
                return simpleObjectProperty;
            }else {
                return null;
            }
        });
        columnCollectTime.setCellValueFactory(new PropertyValueFactory<>("collectTime"));
        formatDateColumn(columnCollectTime);
        updateTable();
    }

    public void updateTable(){
        QueryAllService queryAllService = applicationContext.getBean(QueryAllService.class);
        queryAllService.setCategory(Category.Song);
        queryAllService.setOnSucceeded(event -> {
            tableViewSong.setItems(queryAllService.getValue());
        });
        progressIndicator.visibleProperty().bind(queryAllService.runningProperty());
        queryAllService.start();
    }

    private void formatDateColumn(TableColumn tableColumn){
        tableColumn.setCellFactory(c -> {
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
                AlertUtils.showError("还没有选中歌曲");
            }else {
                DeleteByIDService deleteByIDService = applicationContext.getBean(DeleteByIDService.class);
                deleteByIDService.setCategory(Category.Song);
                deleteByIDService.setId(song.getId());
                progressIndicator.visibleProperty().bind(deleteByIDService.runningProperty());
                deleteByIDService.setOnSucceeded(event -> {
                    if (deleteByIDService.getValue().equals("success")){
                        AlertUtils.showInformation("删除成功");
                        updateTable();
                    }else {
                        AlertUtils.showInformation("删除失败");
                    }
                });
                deleteByIDService.start();
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
