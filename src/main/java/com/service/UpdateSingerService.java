package com.service;

import com.config.ServerConfig;
import com.controller.popup.SingerUpdateController;
import com.controller.tabcontent.TabSingerController;
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

/**
 * @author super lollipop
 * @date 20-2-25
 */
@Service
@Scope("prototype")
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
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                        addTextBody("id",String.valueOf(singerUpdateController.getSelectedSinger().getId()),ContentType.create("text/pain",Charset.forName("UTF-8"))).
                        addTextBody("name",singerUpdateController.getTfName().getText(), ContentType.create("text/pain", Charset.forName("UTF-8")));
                String birthday;
                if (singerUpdateController.getDpBirthday().getValue() != null){
                    int day = singerUpdateController.getDpBirthday().getValue().getDayOfMonth();
                    int month = singerUpdateController.getDpBirthday().getValue().getMonthValue();
                    int year = singerUpdateController.getDpBirthday().getValue().getYear();
                    birthday = year + "/" + month + "/" + day; //xxxx/xx/xx

                }else {
                    birthday = "";
                }
                try {
                    multipartEntityBuilder.addTextBody("birthday",birthday,ContentType.create("text/pain",Charset.forName("UTF-8")));

                    multipartEntityBuilder.addTextBody("height",singerUpdateController.getTfHeight().getText().trim(),ContentType.create("text/pain",Charset.forName("UTF-8")));

                    multipartEntityBuilder.addTextBody("weight",singerUpdateController.getTfWeight().getText().trim(),ContentType.create("text/pain",Charset.forName("UTF-8")));

                    multipartEntityBuilder.addTextBody("constellation",singerUpdateController.getCbConstellation().getValue(),ContentType.create("text/pain",Charset.forName("UTF-8")));
                    String description;
                    if (singerUpdateController.getTaDescription().getText() == null){
                        description = "";
                    }else {
                        description = singerUpdateController.getTaDescription().getText().trim();
                    }
                    multipartEntityBuilder.addTextBody("description",description,ContentType.create("text/pain",Charset.forName("UTF-8")));

                    String responseString = HttpClientUtils.executePost(url,multipartEntityBuilder.build());
                    System.out.println(responseString);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return task;
    }
}
