package com.service;

import com.config.Category;
import com.config.ServerConfig;
import com.util.HttpClientUtils;
import com.util.ParserUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.List;

public class QueryAllService extends Service<ObservableList> {

    /**枚举当前要查询的是哪个种类*/
    private Category category;

    public void setCategory(Category category) {
        this.category = category;
    }

    public QueryAllService() {
    }

    public QueryAllService(Category category) {
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
                        url = ServerConfig.getInstance().getSingerURL() + "/queryAll";
                        break;
                    }
                    case Album:{
                        url = ServerConfig.getInstance().getAlbumURL() + "/queryAll";
                        break;
                    }
                    case Song:{
                        url = ServerConfig.getInstance().getSongURL() + "/queryAll";
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
