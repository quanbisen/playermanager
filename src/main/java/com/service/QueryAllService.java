package com.service;

import com.config.Category;
import com.config.ServerConfig;
import com.util.HttpClientUtils;
import com.util.ParserUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Scope("prototype")
public class QueryAllService extends javafx.concurrent.Service<ObservableList> {

    private ServerConfig serverConfig;

    /**枚举当前要查询的是哪个种类*/
    private Category category;

    @Autowired
    public void constructor(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    protected Task<ObservableList> createTask() {
        Task<ObservableList> task = new Task<ObservableList>() {
            @Override
            protected ObservableList call() throws Exception {
                String url = null;
                switch (category){
                    case Singer:{
                        url = serverConfig.getSingerURL() + "/queryAll";
                        break;
                    }
                    case Album:{
                        url = serverConfig.getAlbumURL() + "/queryAll";
                        break;
                    }
                    case Song:{
                        url = serverConfig.getSongURL() + "/queryAll";
                        break;
                    }
                    default:
                }
                String responseString = HttpClientUtils.executeGet(url);
                List list = ParserUtils.parseResponseStringList(responseString,category);
                ObservableList observableList = FXCollections.observableArrayList();
                observableList.addAll(list);
                return observableList;
            }
        };
        return task;
    }
}
