package com.service;

import com.config.ServerConfig;
import com.controller.popup.SingerInsertController;
import com.controller.tabcontent.TabSingerController;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author super lollipop
 * @date 20-2-24
 */
@Service
@Scope("prototype")
public class InsertSingerService extends javafx.concurrent.Service<Void> {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private SingerInsertController singerInsertController;

    @Resource
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
                    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                            addTextBody("name",singerInsertController.getTfName().getText(),ContentType.create("text/pain",Charset.forName("UTF-8"))).
                            addBinaryBody("bytes",singerInsertController.getImageBytes(),ContentType.DEFAULT_BINARY,fileName);
                    if (singerInsertController.getDpBirthday().getValue() != null){
                        int day = singerInsertController.getDpBirthday().getValue().getDayOfMonth();
                        int month = singerInsertController.getDpBirthday().getValue().getMonthValue();
                        int year = singerInsertController.getDpBirthday().getValue().getYear();
                        String birthday = year + "/" + month + "/" + day; //xxxx/xx/xx
                        multipartEntityBuilder.addTextBody("birthday",birthday,ContentType.create("text/pain",Charset.forName("UTF-8")));
                    }
                    if (!singerInsertController.getTfHeight().getText().trim().equals("")){
                        multipartEntityBuilder.addTextBody("height",singerInsertController.getTfHeight().getText().trim(),ContentType.create("text/pain",Charset.forName("UTF-8")));
                    }
                    if (!singerInsertController.getTfWeight().getText().trim().equals("")){
                        multipartEntityBuilder.addTextBody("weight",singerInsertController.getTfWeight().getText().trim(),ContentType.create("text/pain",Charset.forName("UTF-8")));
                    }
                    if (singerInsertController.getCbConstellation().getValue() != null && !singerInsertController.getCbConstellation().getValue().equals("")){
                        multipartEntityBuilder.addTextBody("constellation",singerInsertController.getCbConstellation().getValue(),ContentType.create("text/pain",Charset.forName("UTF-8")));
                    }
                    if (!singerInsertController.getTaDescription().getText().trim().equals("")){
                        multipartEntityBuilder.addTextBody("description",singerInsertController.getTaDescription().getText().trim(),ContentType.create("text/pain",Charset.forName("UTF-8")));
                    }
                    try {
                        String responseString = HttpClientUtils.executePost(url,multipartEntityBuilder.build());
                        System.out.println(responseString);
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
