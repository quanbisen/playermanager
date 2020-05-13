package com.application;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;


@Component
public final class SpringFXMLLoader{

    private ApplicationContext applicationContext;

    /**注入Spring上下文对象*/
    @Autowired
    public void constructor(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }


    /**
     * 获取一个ControllerFactory被SpringBeanFactory管理的FXMLLoader对象
     * @param resource fxml文件的路径
     * */
    public FXMLLoader getLoader(String resource){
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(resource));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        return fxmlLoader;
    }

}
