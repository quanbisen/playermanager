package com.service;

import com.alibaba.fastjson.JSON;
import com.config.ServerConfig;
import com.controller.tabcontent.TabSingerController;
import com.pojo.Singer;
import com.util.HttpClientUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-24
 */
@Service
@Scope("prototype")
public class QueryAllSingerService extends javafx.concurrent.Service<ObservableList<Singer>> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TabSingerController tabSingerController;

    @Override
    protected Task<ObservableList<Singer>> createTask() {
        Task<ObservableList<Singer>> task = new Task<ObservableList<Singer>>() {
            @Override
            protected ObservableList<Singer> call() throws Exception {
                String url = applicationContext.getBean(ServerConfig.class).getSingerURL()+ "/queryAll";
                String responseString = HttpClientUtils.executeGet(url);
                List<Singer> singerList = JSON.parseArray(responseString,Singer.class);
                ObservableList<Singer> observableSingerList = FXCollections.observableArrayList();
                observableSingerList.addAll(singerList);
                return observableSingerList;
            }
        };
        return task;
    }
}
