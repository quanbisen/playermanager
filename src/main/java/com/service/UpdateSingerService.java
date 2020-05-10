package com.service;

import com.alibaba.fastjson.JSONObject;
import com.config.ServerConfig;
import com.controller.popup.SingerUpdateController;
import com.controller.content.TabSingerController;
import com.pojo.Singer;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-2-25
 */
@Service
@Scope("singleton")
public class UpdateSingerService extends javafx.concurrent.Service<Void> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SingerUpdateController singerUpdateController;

    @Autowired
    private TabSingerController tabSingerController;

    @Override
    protected Task<Void> createTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String url = applicationContext.getBean(ServerConfig.class).getSingerURL() + "/update";
                Singer singer = new Singer();
                singer.setId(singerUpdateController.getSelectedSinger().getId());
                singer.setName(singerUpdateController.getTfName().getText());
                if (singerUpdateController.getDpBirthday().getValue() != null){
                    singer.setBirthday(Date.from(singerUpdateController.getDpBirthday().getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                if (singerUpdateController.getTaDescription().getText() != null
                        && !singerUpdateController.getTaDescription().getText().trim().equals("")){
                    singer.setDescription(singerUpdateController.getTaDescription().getText());
                }
                if (!singerUpdateController.getTfHeight().getText().trim().equals("")){
                    try {
                        singer.setHeight(Float.parseFloat(singerUpdateController.getTfHeight().getText().trim()));
                    }catch (Exception e){e.printStackTrace();}
                }
                if (!singerUpdateController.getTfWeight().getText().trim().equals("")){
                    try {
                        singer.setWeight(Float.parseFloat(singerUpdateController.getTfWeight().getText().trim()));
                    }catch (Exception e){e.printStackTrace();}
                }
                if (!singerUpdateController.getCbConstellation().getValue().equals("")){
                    singer.setConstellation(singerUpdateController.getCbConstellation().getValue());
                }
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                        addTextBody("singer", JSONObject.toJSONString(singer),ContentType.create("application/json",Charset.forName("UTF-8")));
                String responseString = HttpClientUtils.executePost(url,multipartEntityBuilder.build());
                Platform.runLater(()->{
                    singerUpdateController.getTaDescription().getScene().getWindow().hide();    //关闭窗口
                    if (responseString.equals("success")){
                        AlertUtils.showInformation("修改歌手成功");
                        tabSingerController.updateTable();
                    }else {
                        AlertUtils.showError("修改歌手失败");
                    }
                });
                return null;
            }
        };
        return task;
    }
}
