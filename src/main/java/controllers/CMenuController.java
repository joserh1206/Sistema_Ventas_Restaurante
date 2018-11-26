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
    @FXML private TextField inputAddress;
    @FXML private CheckBox checkBoxExpress;

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


    @FXML
    void payOrder(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        Connection connection = DBConnection.Companion.getConnection();
        int count = DBConnection.Companion.getOrdersCount(connection) + 1;
        if(checkBoxExpress.isSelected()) {
            DBConnection.Companion.processOrder(connection, Menu.userName, inputAddress.getText(),
                    Integer.valueOf(textTotal.getText()), choiceBOffice.getValue());
        } else {
            DBConnection.Companion.processOrder(connection, Menu.userName, inputAddress.getPromptText(),
                    Integer.valueOf(textTotal.getText()), choiceBOffice.getValue());
        }
        for (Object obj :
                Menu.orderList) {
            if (obj.getClass() == Dish.class){
                DBConnection.Companion.addOrderDetails(connection, count, ((Dish) obj).getName(), 1);
            } else {
                DBConnection.Companion.addOrderDetails(connection, count, ((Combo) obj).getName(), 2);
            }
        }
        WindowBuilder.Companion.createPopUp("../views/cRating.fxml", event, "Califique nuestro servicio");
        //TODO: Clean up tables and labels
    }

    @FXML
    void enableAddressinput(ActionEvent event) {
        if (checkBoxExpress.isSelected()){
            inputAddress.disableProperty().setValue(false);
        } else {
            inputAddress.disableProperty().setValue(true);
        }
    }

    private void setTableViewCombo() {
        TableColumn<Combo, String> tcNumber = new TableColumn<>("Numero");
        TableColumn<Combo, String> tcName = new TableColumn<>("Nombre");
        TableColumn<Combo, String> tcPrice = new TableColumn<>("Precio");
        TableColumn<Combo, String> tcPTime = new TableColumn<>("Tiempo de preparacion");

        tableCombos.getColumns().addAll(tcNumber, tcName, tcPrice, tcPTime);
        tableCombos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcNumber.setStyle("-fx-alignment: CENTER;");
        tcName.setStyle("-fx-alignment: CENTER;");
        tcPrice.setStyle("-fx-alignment: CENTER;");
        tcPTime.setStyle("-fx-alignment: CENTER;");

        tcNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<>("pTime"));
    }

    private void setTableViewMenu() {
        TableColumn<Dish, String> tcName = new TableColumn<>("Nombre");
        TableColumn<Dish, String> tcType = new TableColumn<>("Tipo");
        TableColumn<Dish, String> tcPrice = new TableColumn<>("Precio");
        TableColumn<Dish, String> tcPTime = new TableColumn<>("Tiempo de preparacion");

        tableMenu.getColumns().addAll(tcName, tcType, tcPrice, tcPTime);
        tableMenu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcName.setStyle("-fx-alignment: CENTER;");
        tcType.setStyle("-fx-alignment: CENTER;");
        tcPrice.setStyle("-fx-alignment: CENTER;");
        tcPTime.setStyle("-fx-alignment: CENTER;");

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<>("pTime"));
    }

    private void setTableViewOrder() {
        TableColumn<Dish, String> tcName = new TableColumn<>("Nombre");
        TableColumn<Dish, String> tcPrice = new TableColumn<>("Precio");

        tableBill.getColumns().addAll(tcName, tcPrice);
        tableBill.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcName.setStyle("-fx-alignment: CENTER;");
        tcPrice.setStyle("-fx-alignment: CENTER;");

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
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
