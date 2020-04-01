package com.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

/**
 * @author super lollipop
 * @date 20-2-21
 */
public class FXManager extends Application {

    /**Spring上下文*/
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        /**Spring配置文件路径*/
        String APPLICATION_CONTEXT_PATH = "/config/application-context.xml";
        applicationContext = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_PATH);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = applicationContext.getBean(SpringFXMLLoader.class).getLoader("/fxml/main/main.fxml");
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("音乐资源管理");
        primaryStage.setScene(scene);
        primaryStage.show();  //显示主舞台
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/image/ApplicationIcon.png",32,32,true,false));
        // 获取屏幕可视化的宽高（Except TaskBar），把窗体设置在可视化的区域居中
        primaryStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - primaryStage.getWidth()) / 2.0);
        primaryStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - primaryStage.getHeight()) / 2.0);
    }

    @Override
    public void stop() {
        applicationContext.close();
    }


}
