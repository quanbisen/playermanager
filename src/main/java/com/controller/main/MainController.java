package com.controller.main;

import com.application.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import java.io.IOException;

/**
 * @author super lollipop
 * @date 20-2-22
 */
@Controller
public class MainController {

    @FXML
    private TabPane tabPane;

    @Autowired
    private ApplicationContext applicationContext;

    public void initialize() throws IOException {

        FXMLLoader tabSongLoader = applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/tabcontent/tab-song.fxml");
        tabPane.getTabs().get(0).setContent(tabSongLoader.load());

        FXMLLoader tabSingerLoader = applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/tabcontent/tab-singer.fxml");
        tabPane.getTabs().get(1).setContent(tabSingerLoader.load());
    }
}
