package com.service;

import com.alibaba.fastjson.JSONObject;
import com.config.ServerConfig;
import com.controller.popup.SingerInsertController;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-2-24
 */
@Service
@Scope("singleton")
public class InsertSingerService extends javafx.concurrent.Service<Void> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SingerInsertController singerInsertController;

    @Autowired
    private TabSingerController tabSingerController;

    @Override
    protected Task<Void> createTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    //获取歌手图片的文件名
                    String fileName = singerInsertController.getImageFile().getName();
                    String url = applicationContext.getBean(ServerConfig.class).getSingerURL() + "/insert";
                    Singer singer = new Singer();
                    singer.setName(singerInsertController.getTfName().getText());
                    if (singerInsertController.getDpBirthday().getValue() != null){ //设置生日
                        singer.setBirthday(Date.from(singerInsertController.getDpBirthday().getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    }
                    if (!singerInsertController.getTfHeight().getText().trim().equals("")){
                        singer.setHeight(Float.parseFloat(singerInsertController.getTfHeight().getText().trim()));
                    }
                    if (!singerInsertController.getTfWeight().getText().trim().equals("")){
                        singer.setWeight(Float.parseFloat(singerInsertController.getTfWeight().getText().trim()));
                    }
                    if (!StringUtils.isEmpty(singerInsertController.getCbConstellation().getValue())){
                        singer.setConstellation(singerInsertController.getCbConstellation().getValue());
                    }
                    if (!singerInsertController.getTaDescription().getText().trim().equals("")){
                        singer.setDescription(singerInsertController.getTaDescription().getText().trim());
                    }
                    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                            addTextBody("singer",JSONObject.toJSONString(singer),ContentType.create("application/json",Charset.forName("UTF-8"))).
                            addBinaryBody("bytes",singerInsertController.getImageBytes(),ContentType.DEFAULT_BINARY,fileName);
                    try {
                        String responseString = HttpClientUtils.executePost(url,multipartEntityBuilder.build());
                        Platform.runLater(()->{
                            singerInsertController.getTaDescription().getScene().getWindow().hide();    //关闭窗口
                            if (responseString.equals("success")){
                                AlertUtils.showInformation("新增歌手成功");
                                tabSingerController.updateTable();
                            }else {
                                AlertUtils.showError("新增歌手失败");
                            }
                        });
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){e.printStackTrace();}
                return null;
            }
        };
        return task;
    }


}
