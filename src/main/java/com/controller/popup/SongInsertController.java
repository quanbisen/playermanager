package com.controller.popup;

import com.service.InsertSongService;
import com.util.TimeUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author super lollipop
 * @date 20-2-23
 */
@Controller
public class SongInsertController {



    @FXML
    private TextField tfSongPath;

    @FXML
    private TextField tfLyricPath;

    @FXML
    private Button btnChooseSongFile;

    @FXML
    private Button btnChooseLyricFile;

    @FXML
    private ImageView ivAlbum;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfArtist;

    @FXML
    private TextField tfAlbum;

    @FXML
    private Label labSize;

    @FXML
    private Label labTotalTime;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnCancel;

    private File songFile;

    private File lyricFile;

    private File albumFile;

    private byte[] albumBytes;

    @Resource
    private ApplicationContext applicationContext;

    public TextField getTfTitle() {
        return tfTitle;
    }

    public TextField getTfArtist() {
        return tfArtist;
    }

    public TextField getTfAlbum() {
        return tfAlbum;
    }

    public Label getLabSize() {
        return labSize;
    }

    public Label getLabTotalTime() {
        return labTotalTime;
    }

    public File getSongFile() {
        return songFile;
    }

    public File getLyricFile() {
        return lyricFile;
    }

    public File getAlbumFile() {
        return albumFile;
    }

    public byte[] getAlbumBytes() {
        return albumBytes;
    }

    @FXML
    public void onClickedCancel(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    public void onClickedChooseAlbumFile(MouseEvent event) throws IOException {
        FileChooser albumFileChooser = new FileChooser();
        albumFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        albumFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG图片(*.jpg)","*.jpg"),
                new FileChooser.ExtensionFilter("PNG图片(*.png)","*.png")
        );
        albumFile = albumFileChooser.showOpenDialog(ivAlbum.getScene().getWindow());
        if (albumFile != null){
            if (albumFile.length() / 1024 /1024 > 1){ //文件大小大于1m，不允许选择
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示信息");
                alert.setContentText("图片文件大于1M");
                alert.showAndWait();
            }else {
                Image image = new Image("file:" + albumFile.toPath().toString(),150,150,true,true);
                ivAlbum.setImage(image);
                albumBytes = Files.readAllBytes(albumFile.toPath());
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
            tfArtist.setText(artist);
            tfAlbum.setText(album);
            //获取专辑图片数据
            AbstractID3v2Frame abstractID3v2Frame = (AbstractID3v2Frame)mp3File.getID3v2Tag().getFrame("APIC");
            if (abstractID3v2Frame != null){
                FrameBodyAPIC frameBodyAPIC = (FrameBodyAPIC) abstractID3v2Frame.getBody();
                albumBytes = frameBodyAPIC.getImageData();
                Image albumImage = new Image(new ByteArrayInputStream(albumBytes),150,150,true,true);
                ivAlbum.setImage(albumImage);
            }
        }
    }

    /**验证输入的信息完整*/
    private void validateInput(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (songFile == null || songFile.length() ==0
                || lyricFile == null || lyricFile.length() ==0){
            alert.setContentText("歌曲文件或歌词文件选择不完整");
            alert.showAndWait();
        }else if (ivAlbum.getImage() == null){
            alert.setContentText("专辑图片不完整");
            alert.showAndWait();
        }else if (tfTitle.getText().trim().equals("") || tfArtist.getText().trim().equals("")
        ||tfAlbum.getText().trim().equals("")){
            alert.setContentText("歌曲信息输入不完整");
            alert.showAndWait();
        }else {
            //开始上传歌曲

        }
    }

    @FXML
    public void onClickedUpload(ActionEvent event) {
        validateInput();
        InsertSongService insertSongService = applicationContext.getBean(InsertSongService.class);
        progressIndicator.visibleProperty().bind(insertSongService.runningProperty());
        insertSongService.start();
    }

    @FXML
    public void onClickedSaveToLocalFile(ActionEvent actionEvent) throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
        if (songFile != null && songFile.length() > 0){
            MP3File mp3File = new MP3File(songFile);
            AbstractID3v2Tag id3v22Tag;
            if (mp3File.hasID3v2Tag()){ //有ID3v2Tag,取出来
                id3v22Tag = mp3File.getID3v2Tag();
            }else { //否则，新建一个
                id3v22Tag = new ID3v22Tag();
            }
            id3v22Tag.setField(FieldKey.TITLE,tfTitle.getText());
            id3v22Tag.setField(FieldKey.ARTIST,tfArtist.getText());
            id3v22Tag.setField(FieldKey.ALBUM,tfAlbum.getText());
            if (albumFile != null && albumFile.length() > 0){
                AbstractID3v2Frame abstractID3v2Frame = (AbstractID3v2Frame) mp3File.getID3v2Tag().getFrame("APIC");
                FrameBodyAPIC frameBodyAPIC = (FrameBodyAPIC) abstractID3v2Frame.getBody();
                frameBodyAPIC.setImageData(Files.readAllBytes(albumFile.toPath()));
            }
            mp3File.setID3v2Tag(id3v22Tag);
            mp3File.save();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("保存歌曲信息成功");
            alert.showAndWait();
            showSongTagInformation(songFile);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("没有选择歌曲文件");
            alert.showAndWait();
        }

    }
}
