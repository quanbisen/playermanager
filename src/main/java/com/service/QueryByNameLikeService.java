package com.service;

import com.config.Category;
import com.config.ServerConfig;
import com.util.HttpClientUtils;
import com.util.ParserUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import java.nio.charset.Charset;
import java.util.List;

public class QueryByNameLikeService extends Service<ObservableList> {

    private Category category;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    protected Task<ObservableList> createTask() {
        Task<ObservableList> task = new Task<>() {
            @Override
            protected ObservableList call() throws Exception {
                String url = null;
                switch (category) {
                    case Singer: {
                        url = ServerConfig.getInstance().getSingerURL() + "/queryByNameLike";
                        break;
                    }
                    case Album: {
                        url = ServerConfig.getInstance().getAlbumURL() + "/queryByNameLike";
                        break;
                    }
                    case Song: {
                        url = ServerConfig.getInstance().getSongURL() + "/queryByNameLike";
                        break;
                    }
                    default:
                }
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().addTextBody("name", name, ContentType.create("text/plain", Charset.forName("utf-8")));
                String responseString = HttpClientUtils.executePost(url, multipartEntityBuilder.build());
                List list = ParserUtils.parseResponseStringList(responseString, category);
                if (list != null && list.size() > 0) {
                    ObservableList observableList = FXCollections.observableArrayList();
                    observableList.addAll(list);
                    return observableList;
                } else {
                    return null;
                }

            }
        };
        return task;
    }
}
