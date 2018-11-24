package controllers;

import code.*;
import code.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CMenuController implements Initializable {

    @FXML private TableView tableCombos;
    @FXML private TableView tableMenu;
    @FXML private TableView tableBill;
    @FXML private ChoiceBox<String> choiceBOffice;
    @FXML private Text textTotal;
    @FXML private Text textTime;

    int total = 0;
    int totalTime = 0;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Menu.orderList.clear();
            Connection connection = DBConnection.Companion.getConnection();

            setTableViewCombo();
            setTableViewMenu();
            setTableViewOrder();
            setChoiceBOffice(DBConnection.Companion.getBranchOffice(connection));
            tableBill.setItems(Menu.orderList);
            tableCombos.setItems(DBConnection.Companion.viewCombos(connection));
            tableMenu.setItems(DBConnection.Companion.viewDishes(connection));

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void getComboSelected(MouseEvent event) {
        try{
            Combo combo = (Combo) tableCombos.getSelectionModel().getSelectedItem();
            total = total + combo.getPrice();
            totalTime = totalTime + combo.getPTime();
            textTotal.setText(String.valueOf(total));
            textTime.setText(String.valueOf(totalTime));
            Menu.orderList.add(combo);
            tableBill.setItems(Menu.orderList);
            ObservableList<Object> newOrderList = FXCollections.observableArrayList();
            newOrderList.addAll(Menu.orderList);
            tableBill.getItems().clear();
            tableBill.getItems().addAll(newOrderList);

        } catch (NullPointerException n){

        }
    }

    @FXML
    void getDishSelected(MouseEvent event) {
        try{
            Dish dish = (Dish) tableMenu.getSelectionModel().getSelectedItem();
            total = total + dish.getPrice();
            totalTime = totalTime + dish.getPTime();
            textTotal.setText(String.valueOf(total));
            textTime.setText(String.valueOf(totalTime));
            Menu.orderList.add(dish);
            tableBill.setItems(Menu.orderList);
            ObservableList<Object> newOrderList = FXCollections.observableArrayList();
            newOrderList.addAll(Menu.orderList);
            tableBill.getItems().clear();
            tableBill.getItems().addAll(newOrderList);

        } catch (NullPointerException n){

        }
    }

    @FXML
    void deleteElement(MouseEvent event) {
        try {
            Object object = tableBill.getSelectionModel().getSelectedItem();
            if (object.getClass() == Dish.class) {
                Dish dish = (Dish) tableBill.getSelectionModel().getSelectedItem();
                total = total - dish.getPrice();
                totalTime = totalTime - dish.getPTime();
            } else {
                Combo combo = (Combo) tableBill.getSelectionModel().getSelectedItem();
                total = total - combo.getPrice();
                totalTime = totalTime - combo.getPTime();
            }
            Menu.orderList.remove(object);
            tableBill.setItems(Menu.orderList);
            ObservableList<Object> newOrderList = FXCollections.observableArrayList();
            newOrderList.addAll(Menu.orderList);
            tableBill.getItems().clear();
            tableBill.getItems().addAll(newOrderList);
            textTotal.setText(String.valueOf(total));
            textTime.setText(String.valueOf(totalTime));
        } catch (NullPointerException n){

        }
    }

    private void setTableViewCombo() {
        TableColumn<Combo, String> tcNumber = new TableColumn<Combo, String>("Numero");
        TableColumn<Combo, String> tcName = new TableColumn<Combo, String>("Nombre");
        TableColumn<Combo, String> tcPrice = new TableColumn<Combo, String>("Precio");
        TableColumn<Combo, String> tcPTime = new TableColumn<Combo, String>("Tiempo de preparacion");

        tableCombos.getColumns().addAll(tcNumber, tcName, tcPrice, tcPTime);
        tableCombos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tcNumber.setCellValueFactory(new PropertyValueFactory<Combo, String>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<Combo, String>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<Combo, String>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<Combo, String>("pTime"));
    }

    private void setTableViewMenu() {
        TableColumn<Dish, String> tcName = new TableColumn<Dish, String>("Nombre");
        TableColumn<Dish, String> tcType = new TableColumn<Dish, String>("Tipo");
        TableColumn<Dish, String> tcPrice = new TableColumn<Dish, String>("Precio");
        TableColumn<Dish, String> tcPTime = new TableColumn<Dish, String>("Tiempo de preparacion");

        tableMenu.getColumns().addAll(tcName, tcType, tcPrice, tcPTime);
        tableMenu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tcName.setCellValueFactory(new PropertyValueFactory<Dish, String>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<Dish, String>("type"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<Dish, String>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<Dish, String>("pTime"));
    }

    private void setTableViewOrder() {
        TableColumn<Dish, String> tcName = new TableColumn<Dish, String>("Nombre");
        TableColumn<Dish, String> tcPrice = new TableColumn<Dish, String>("Precio");

        tableBill.getColumns().addAll(tcName, tcPrice);
        tableBill.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tcName.setCellValueFactory(new PropertyValueFactory<Dish, String>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<Dish, String>("price"));
    }

    public void setChoiceBOffice(ResultSet resultSet) throws SQLException {

        while (resultSet.next()){
            choiceBOffice.getItems().add(resultSet.getString(1));
        }
        choiceBOffice.getSelectionModel().selectFirst();
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Esta seguro que desea salir?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();
        if(confirmation.getResult() == ButtonType.YES) {
            WindowBuilder.Companion.createWindow("../views/login.fxml", event, "Iniciar sesión");
        } else {
            confirmation.close();
        }
    }

}
