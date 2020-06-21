package com.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class LoadOnlineImageService extends Service<Image> {

    /**默认图片的url*/
    private String defaultURI;

    /**期望的图片url*/
    private String preferURI;

    /**图片宽度*/
    private double width;

    /**图片高度*/
    private double height;

    public void setDefaultURI(String defaultURI) {
        this.defaultURI = defaultURI;
    }

    public void setPreferURI(String preferURI) {
        this.preferURI = preferURI;
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
                Image image = new Image(preferURI,width,height,true,true);
                if (image.isError()) {
                    if (defaultURI != null && !defaultURI.equals("")){
                        image = new Image(defaultURI, width, height, true, true);
                    }else {
                        throw new Exception("默认图片资源未设定");
                    }
                }
                return image;
            }
        };
        return createImageTask;
    }
}
