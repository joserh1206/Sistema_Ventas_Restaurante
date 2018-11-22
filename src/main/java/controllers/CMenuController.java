package controllers;

import code.Combo;
import code.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CMenuController implements Initializable {

    @FXML private TableView tableCombos;
    @FXML private TableView tableMenu;
    @FXML private ChoiceBox choiceBOffice;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection connection = DBConnection.Companion.getConnection();

            setTableViewCombo();
            setTableViewMenu();
            setChoiceBOffice(DBConnection.Companion.getBranchOffice(connection));
            tableCombos.setItems(DBConnection.Companion.viewCombos(connection));
            tableMenu.setItems(DBConnection.Companion.viewDishes(connection));

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setTableViewCombo() {
        TableColumn<Combo, String> tcNumber = new TableColumn<Combo, String>("Numero");
        TableColumn<Combo, String> tcName = new TableColumn<Combo, String>("Nombre");
        TableColumn<Combo, String> tcPrice = new TableColumn<Combo, String>("Precio");
        TableColumn<Combo, String> tcPTime = new TableColumn<Combo, String>("Tiempo de preparacion");

        tableCombos.getColumns().addAll(tcNumber, tcName, tcPrice, tcPTime);

        tcNumber.setCellValueFactory(new PropertyValueFactory<Combo, String>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<Combo, String>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<Combo, String>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<Combo, String>("pTime"));
    }

    private void setTableViewMenu() {
        TableColumn<Combo, String> tcName = new TableColumn<Combo, String>("Nombre");
        TableColumn<Combo, String> tcType = new TableColumn<Combo, String>("Tipo");
        TableColumn<Combo, String> tcPrice = new TableColumn<Combo, String>("Precio");
        TableColumn<Combo, String> tcPTime = new TableColumn<Combo, String>("Tiempo de preparacion");

        tableMenu.getColumns().addAll(tcName, tcType, tcPrice, tcPTime);

        tcName.setCellValueFactory(new PropertyValueFactory<Combo, String>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<Combo, String>("type"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<Combo, String>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<Combo, String>("pTime"));
    }

    public void setChoiceBOffice(ResultSet resultSet) throws SQLException {

        while (resultSet.next()){
            choiceBOffice.getItems().add(resultSet.getString(1));
        }
        choiceBOffice.getSelectionModel().selectFirst();
    }

}
