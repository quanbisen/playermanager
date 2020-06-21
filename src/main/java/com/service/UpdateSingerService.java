package com.service;

import com.alibaba.fastjson.JSONObject;
import com.config.ServerConfig;
import com.controller.popup.SingerUpdateController;
import com.controller.content.TabSingerController;
import com.pojo.Singer;
import com.util.AlertUtils;
import com.util.HttpClientUtils;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-2-25
 */

public class UpdateSingerService extends Service<Void> {

    private SingerUpdateController singerUpdateController;

    private TabSingerController tabSingerController;

    public void setSingerUpdateController(SingerUpdateController singerUpdateController) {
        this.singerUpdateController = singerUpdateController;
    }

    public void setTabSingerController(TabSingerController tabSingerController) {
        this.tabSingerController = tabSingerController;
    }

    @Override
    protected Task<Void> createTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = ServerConfig.getInstance().getSingerURL() + "/update";
                Singer singer = new Singer();
                singer.setId(singerUpdateController.getSelectedSinger().getId());
                singer.setName(singerUpdateController.getTfName().getText());
                if (singerUpdateController.getDpBirthday().getValue() != null) {
                    singer.setBirthday(Date.from(singerUpdateController.getDpBirthday().getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                if (singerUpdateController.getTaDescription().getText() != null
                        && !singerUpdateController.getTaDescription().getText().trim().equals("")) {
                    singer.setDescription(singerUpdateController.getTaDescription().getText());
                }
                if (!singerUpdateController.getTfHeight().getText().trim().equals("")) {
                    try {
                        singer.setHeight(Float.parseFloat(singerUpdateController.getTfHeight().getText().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!singerUpdateController.getTfWeight().getText().trim().equals("")) {
                    try {
                        singer.setWeight(Float.parseFloat(singerUpdateController.getTfWeight().getText().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!singerUpdateController.getCbConstellation().getValue().equals("")) {
                    singer.setConstellation(singerUpdateController.getCbConstellation().getValue());
                }
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().
                        addTextBody("singer", JSONObject.toJSONString(singer), ContentType.create("application/json", Charset.forName("UTF-8")));
                String responseString = HttpClientUtils.executePost(url, multipartEntityBuilder.build());
                Platform.runLater(() -> {
                    singerUpdateController.getTaDescription().getScene().getWindow().hide();    //关闭窗口
                    if (responseString.equals("success")) {
                        AlertUtils.showInformation("修改歌手成功");
                        tabSingerController.updateTable();
                    } else {
                        AlertUtils.showError("修改歌手失败");
                    }
                });
                return null;
            }
        };
        return task;
    }
}
