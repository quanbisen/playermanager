package com.service;

import com.alibaba.fastjson.JSON;
import com.config.ServerConfig;
import com.controller.popup.SingerQueryController;
import com.pojo.Singer;
import com.util.HttpClientUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.List;

/**
 * @author super lollipop
 * @date 20-2-25
 */

public class QuerySingerByNameService extends Service<ObservableList<Singer>> {

    private SingerQueryController singerQueryController;

    @Override
    protected Task<ObservableList<Singer>> createTask() {
        Task<ObservableList<Singer>> task = new Task<ObservableList<Singer>>() {
            @Override
            protected ObservableList<Singer> call() throws Exception {
//                System.out.println(applicationContext.getBean(ServerConfig.class).getSingerURL() + "/query/" + singerQueryController.getTfName().getText());
//                String responseString = HttpClientUtils.executeGet(applicationContext.getBean(ServerConfig.class).getSingerURL() + "/query/" + singerQueryController.getTfName().getText());
//                List<Singer> singerList = JSON.parseArray(responseString, Singer.class);
//                if (singerList != null && singerList.size() > 0){
//                    ObservableList<Singer> observableList = FXCollections.observableArrayList();
//                    observableList.addAll(singerList);
//                    return observableList;
//                }else {
//                    return null;
//                }
                return null;
            }
        };
        return task;
    }
}
