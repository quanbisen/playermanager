package com.service;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Scope("prototype")
public class LoadOnlineImageService extends javafx.concurrent.Service<Image> {

    private String defaultURI;

    private String optimizeURI;

    private double width;

    private double height;

    public void setDefaultURI(String defaultURI) {
        this.defaultURI = defaultURI;
    }

    public void setOptimizeURI(String optimizeURI) {
        this.optimizeURI = optimizeURI;
    }

    public void setImageSize(double width , double height){
        this.width = width;
        this.height = height;
    }

    @Override
    protected Task<Image> createTask() {
        Task<Image> createImageTask = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                Image image = new Image(optimizeURI,width,height,true,true);
                if (image.isError() && !StringUtils.isEmpty(defaultURI)) {
                    image = new Image(defaultURI, width, height, true, true);
                }
                return image;
            }
        };
        return createImageTask;
    }
}
