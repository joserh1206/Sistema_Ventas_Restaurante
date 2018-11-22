package controllers;

import code.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AMenuController implements Initializable {

    @FXML private ChoiceBox choiceProductType;

    public void initialize(URL location, ResourceBundle resources) {
        Connection connection;
        try {
            connection = DBConnection.Companion.getConnection();
            ResultSet resultSet = DBConnection.Companion.getDishType(connection);
            while (resultSet.next()){
                choiceProductType.getItems().add(resultSet.getString(1));
            }
            connection.close();
            choiceProductType.getSelectionModel().selectFirst();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void employeeControl(ActionEvent event) throws IOException {
        WindowBuilder.Companion.createWindow("../views/eRegister.fxml", event, "Registro de empleados");
    }

}
