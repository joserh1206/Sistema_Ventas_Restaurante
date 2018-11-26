package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class CRatingController{

    @FXML
    void finishOrder(ActionEvent event) {
        WindowBuilder.Companion.closeWindow(event);
        new Alert(Alert.AlertType.INFORMATION, "¡Su orden estará lista pronto!").showAndWait();
    }

}
