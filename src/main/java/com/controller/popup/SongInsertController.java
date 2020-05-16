package com.controller.popup;

import com.config.Category;
import com.config.LocalConfig;
import com.pojo.Album;
import com.pojo.Singer;
import com.service.InsertSongService;
import com.service.QueryByNameLikeService;
import com.util.AlertUtils;
import com.util.FileUtils;
import com.util.StageUtils;
import com.util.TimeUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-23
 */
@Controller
public class SongInsertController {

    @FXML
    private ImageView ivAlbum;

    @FXML
    private TextField tfSongPath;

    @FXML
    private Button btnChooseSongFile;

    @FXML
    private TextField tfLyricPath;

    @FXML
    private Button btnChooseLyricFile;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfArtist;

    @FXML
    private TextField tfAlbum;

    @FXML
    private Label labTotalTime;

    @FXML
    private Label labSize;

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnCancel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TableView<Album> tableViewAlbum;

    @FXML
    private TextField tfAlbumForSearch;

    @FXML
    private TableView<Singer> tableViewSinger;

    @FXML
    private TableColumn<Singer,String> columnSingerID;

    @FXML
    private TableColumn<Singer,String> columnSingerName;

    @FXML
    private TableColumn<Album,String> columnAlbumID;

    @FXML
    private TableColumn<Album,String> columnAlbumName;

    @FXML
    private TextField tfArtistForSearch;

    private ApplicationContext applicationContext;

    private File songFile;

    private File albumFile;

    private File lyricFile;

    @Autowired
    public void constructor(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public ImageView getIvAlbum() {
        return ivAlbum;
    }

    public TextField getTfTitle() {
        return tfTitle;
    }

    public TextField getTfArtist() {
        return tfArtist;
    }

    public TextField getTfAlbum() {
        return tfAlbum;
    }

    public Label getLabTotalTime() {
        return labTotalTime;
    }

    public Label getLabSize() {
        return labSize;
    }

    public File getSongFile() {
        return songFile;
    }

    public File getAlbumFile() {
        return albumFile;
    }

    public File getLyricFile() {
        return lyricFile;
    }

    public void initialize(){
        columnSingerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnSingerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAlbumID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAlbumName.setCellValueFactory(new PropertyValueFactory<>("name"));

        /**judge weather clear tfArtist.UserData***/
        tfArtist.textProperty().addListener((observable, oldValue, newValue) -> {

        });

    }

    @FXML
    public void onClickedCancel(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    public void onClickedChooseAlbumFile(MouseEvent event) throws IOException {
        albumFile = StageUtils.getImageFileChooser().showOpenDialog(ivAlbum.getScene().getWindow());
        if (albumFile != null){
            if (albumFile.length() / 1024 /1024 > 1){ //文件大小大于1m，不允许选择
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示信息");
                alert.setContentText("图片文件大于1M");
                alert.showAndWait();
            }else {
                Image image = new Image("file:" + albumFile.toPath().toString(),150,150,true,true);
                ivAlbum.setImage(image);
            }
        }
    }

    @FXML
    public void onClickedChooseLyricFile(ActionEvent event) {
        FileChooser lyricFileChooser = new FileChooser();
        lyricFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        lyricFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("歌词文件(*.lrc)","*.lrc"));
        lyricFile = lyricFileChooser.showOpenDialog(btnChooseSongFile.getScene().getWindow());
        if (lyricFile != null){
            tfLyricPath.setText(lyricFile.getPath());
        }
    }

    @FXML
    public void onClickedChooseSongFile(ActionEvent event) throws ReadOnlyFileException, CannotReadException, TagException, InvalidAudioFrameException, IOException {
        FileChooser songFileChooser = new FileChooser();
        songFileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("音乐文件(*.mp3)","*.mp3")
        );
        songFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        songFile = songFileChooser.showOpenDialog(btnChooseSongFile.getScene().getWindow());
        if (songFile != null && songFile.getPath().endsWith(".mp3")){
            tfSongPath.setText(songFile.getPath());
            showSongTagInformation(songFile);
        }
    }

    private void showSongTagInformation(File file) throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
        tfTitle.setText("");
        tfArtist.setText("");
        tfAlbum.setText("");
        ivAlbum.setImage(null);

        AudioFile audioFile = AudioFileIO.read(file);
        MP3File mp3File = new MP3File(file);
        int second = audioFile.getAudioHeader().getTrackLength();
        String totalTime = TimeUtils.toString(second);
        labTotalTime.setText(totalTime);
        String m =String.valueOf(file.length()/1024.0/1024.0);
        String size=m.substring(0, m.indexOf(".")+3)+"MB";   //文件大小
        labSize.setText(size);
        if (mp3File.hasID3v2Tag()){     //如果存在ID3v2标签，读取
            String title = mp3File.getID3v2Tag().getFirst(FieldKey.TITLE);
            String artist = mp3File.getID3v2Tag().getFirst(FieldKey.ARTIST);
            String album = mp3File.getID3v2Tag().getFirst(FieldKey.ALBUM);
            tfTitle.setText(title);
            tfArtistForSearch.setText(artist);
            tfAlbumForSearch.setText(album);
            QueryByNameLikeService queryByNameLikeService = applicationContext.getBean(QueryByNameLikeService.class);
            queryByNameLikeService.setName(artist);
            queryByNameLikeService.setCategory(Category.Singer);
            queryByNameLikeService.setOnSucceeded(event -> {
                tableViewSinger.setItems(queryByNameLikeService.getValue());
            });
            queryByNameLikeService.start();
            progressIndicator.visibleProperty().bind(queryByNameLikeService.runningProperty());


            //获取专辑图片数据
            AbstractID3v2Frame abstractID3v2Frame = (AbstractID3v2Frame)mp3File.getID3v2Tag().getFrame("APIC");
            if (abstractID3v2Frame != null){
                FrameBodyAPIC frameBodyAPIC = (FrameBodyAPIC) abstractID3v2Frame.getBody();
                albumFile = FileUtils.getFile(frameBodyAPIC.getImageData(),applicationContext.getBean(LocalConfig.class).getConfigPath().toString(),"tmp");
                System.out.println(albumFile.length());
                Image albumImage = new Image(new FileInputStream(albumFile),150,150,true,true);
                ivAlbum.setImage(albumImage);
            }
        }
    }

    /**验证输入的信息完整*/
    private boolean validateInput(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (songFile == null || songFile.length() ==0
                || lyricFile == null || lyricFile.length() ==0){
            AlertUtils.showError("歌曲文件或歌词文件选择不完整");
            return false;
        }else if (ivAlbum.getImage() == null){
            AlertUtils.showError("专辑图片不完整");
            return false;
        }else if (tfTitle.getText().trim().equals("") || tfArtist.getText().trim().equals("")
        ||tfAlbum.getText().trim().equals("")){
            AlertUtils.showError("歌曲信息输入不完整");
            return false;
        }else if (tfArtist.getUserData() == null || ((List<Singer>)tfArtist.getUserData()).size() == 0){
            AlertUtils.showError("歌曲信息输入不完整");
            return false;
        }else {
            return true;
        }
    }

    @FXML
    public void onClickedUpload(ActionEvent event) {
        if (validateInput()){
            InsertSongService insertSongService = applicationContext.getBean(InsertSongService.class);
            progressIndicator.visibleProperty().bind(insertSongService.runningProperty());
            insertSongService.start();
        }
    }

    @FXML
    public void onClickedQueryAlbum(ActionEvent actionEvent) {
        String album = tfAlbumForSearch.getText().trim();
        if (!album.equals("")){
            QueryByNameLikeService queryByNameLikeService = applicationContext.getBean(QueryByNameLikeService.class);
            queryByNameLikeService.setName(album);
            queryByNameLikeService.setCategory(Category.Album);
            progressIndicator.visibleProperty().bind(queryByNameLikeService.runningProperty());
            queryByNameLikeService.start();
            queryByNameLikeService.setOnSucceeded(event -> tableViewAlbum.setItems(queryByNameLikeService.getValue()));
        }

    }

    @FXML
    public void onClickedQueryArtist(ActionEvent actionEvent) {
        String artist = tfArtistForSearch.getText().trim();
        if (!artist.equals("")){
            QueryByNameLikeService queryByNameLikeService = applicationContext.getBean(QueryByNameLikeService.class);
            queryByNameLikeService.setName(artist);
            queryByNameLikeService.setCategory(Category.Singer);
            progressIndicator.visibleProperty().bind(queryByNameLikeService.runningProperty());
            queryByNameLikeService.start();
            queryByNameLikeService.setOnSucceeded(event -> tableViewSinger.setItems(queryByNameLikeService.getValue()));
        }
    }

    @FXML
    public void onClickedTableSinger(MouseEvent mouseEvent) {
        if (tableViewSinger.getItems() != null && tableViewSinger.getItems().size() > 0){
            Singer singer = tableViewSinger.getSelectionModel().getSelectedItem();  //获取选择的歌手
            List<Singer> singerList;
            if (tfArtist.getUserData() == null){
                singerList = new LinkedList<>();
                tfArtist.setText(singer.getName());
            }else {
                singerList = (List<Singer>) tfArtist.getUserData();
                StringBuilder stringBuilder = new StringBuilder(tfArtist.getText()).append("/");
                tfArtist.setText(stringBuilder.append(singer.getName()).toString());
            }
            singerList.add(singer);
            tfArtist.setUserData(singerList);
        }
    }

    @FXML
    public void onClickedTableAlbum(MouseEvent mouseEvent) {
        if (tableViewAlbum.getItems() != null && tableViewAlbum.getItems().size() > 0){
            Album album = tableViewAlbum.getSelectionModel().getSelectedItem();
            tfAlbum.setUserData(album);
            tfAlbum.setText(album.getName());
        }
    }

    /**annotation for later develop.*/
//    @FXML
//    public void onClickedSaveToLocalFile(ActionEvent actionEvent) throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
//        if (songFile != null && songFile.length() > 0){
//            MP3File mp3File = new MP3File(songFile);
//            AbstractID3v2Tag id3v22Tag;
//            if (mp3File.hasID3v2Tag()){ //有ID3v2Tag,取出来
//                id3v22Tag = mp3File.getID3v2Tag();
//            }else { //否则，新建一个
//                id3v22Tag = new ID3v22Tag();
//            }
//            id3v22Tag.setField(FieldKey.TITLE,tfTitle.getText());
//            id3v22Tag.setField(FieldKey.ARTIST,tfArtist.getText());
//            id3v22Tag.setField(FieldKey.ALBUM,tfAlbum.getText());
//            if (albumFile != null && albumFile.length() > 0){
//                AbstractID3v2Frame abstractID3v2Frame = (AbstractID3v2Frame) mp3File.getID3v2Tag().getFrame("APIC");
//                FrameBodyAPIC frameBodyAPIC = (FrameBodyAPIC) abstractID3v2Frame.getBody();
//                frameBodyAPIC.setImageData(Files.readAllBytes(albumFile.toPath()));
//            }
//            mp3File.setID3v2Tag(id3v22Tag);
//            mp3File.save();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText("保存歌曲信息成功");
//            alert.showAndWait();
//            showSongTagInformation(songFile);
//        }else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("没有选择歌曲文件");
//            alert.showAndWait();
//        }
//    }
}
