package com.application;

import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public final class SpringFXMLLoader {

    /**注入Spring上下文对象*/
    @Autowired
    private ApplicationContext applicationContext;

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
