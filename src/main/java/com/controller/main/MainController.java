package com.controller.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import java.io.IOException;

/**
 * @author super lollipop
 * @date 20-2-22
 */

public class MainController {

    @FXML
    private TabPane tabPane;

    public void initialize() throws IOException {

        FXMLLoader tabSongLoader = new FXMLLoader(getClass().getResource("/fxml/content/tab-song.fxml"));
        tabPane.getTabs().get(0).setContent(tabSongLoader.load());

        FXMLLoader tabSingerLoader = new FXMLLoader(getClass().getResource("/fxml/content/tab-singer.fxml"));
        tabPane.getTabs().get(1).setContent(tabSingerLoader.load());

        FXMLLoader tabAlbumLoader = new FXMLLoader(getClass().getResource("/fxml/content/tab-album.fxml"));
        tabPane.getTabs().get(2).setContent(tabAlbumLoader.load());
    }
}
