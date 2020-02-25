package com.util;

import javafx.scene.control.Alert;

/**
 * @author super lollipop
 * @date 20-2-24
 */
public class AlertUtils {

    public static void showInformation(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        show(text,alert);
    }

    public static void showWarning(String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        show(text,alert);
    }

    public static void showError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        show(text,alert);
    }

    private static void show(String text,Alert alert){
        alert.setContentText(text);
        alert.showAndWait();
    }
}
