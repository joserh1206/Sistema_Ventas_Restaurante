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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AMenuController implements Initializable {

    @FXML private ChoiceBox<String> choiceProductType;
    @FXML private TableView tableMenu;
    @FXML private TableView tableComboCreator;
    @FXML private TableView tableCombos;
    @FXML private TextField inputDishName;
    @FXML private TextField inputDishPrice;
    @FXML private TextField inputPreparationTime;
    @FXML private TextField inputComboPrice;
    @FXML private TextField inputComboName;

    public void initialize(URL location, ResourceBundle resources) {
        Connection connection;
        try {
            DishCombo.dishComboList.clear();
            connection = DBConnection.Companion.getConnection();
            ResultSet resultSet = DBConnection.Companion.getDishType(connection);
            while (resultSet.next()){
                choiceProductType.getItems().add(resultSet.getString(1));
            }
            setTableViewMenu();
            setTableViewCombo();
            setTableViewComboMenu();
            tableCombos.setItems(DBConnection.Companion.viewAllCombos(connection));
            tableMenu.setItems(DBConnection.Companion.viewAllDishes(connection));
            connection.close();
            choiceProductType.getSelectionModel().selectFirst();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addDish(ActionEvent event) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.Companion.getConnection();
        DBConnection.Companion.addDish(connection, inputDishName.getText(), Integer.valueOf(inputDishPrice.getText()),
                Integer.valueOf(inputPreparationTime.getText()), choiceProductType.getValue());
        Button button = new Button("Desactivar");
        Menu.dishList.add(new Dish(inputDishName.getText(), choiceProductType.getValue(),
                Integer.valueOf(inputDishPrice.getText()), Integer.valueOf(inputPreparationTime.getText()), 1, button));
        ObservableList<Dish> newDishes = FXCollections.observableArrayList();
        newDishes.addAll(Menu.dishList);
        tableMenu.getItems().clear();
        tableMenu.getItems().addAll(newDishes);
        new Alert(Alert.AlertType.INFORMATION, "Plato ingresado correctamente en el inventario").showAndWait();
    }


    @FXML
    void getSelectedDish(MouseEvent event) {
        try{
            Dish dish = (Dish) tableMenu.getSelectionModel().getSelectedItem();
            DishCombo.dishComboList.add(dish);
            tableComboCreator.setItems(DishCombo.dishComboList);
            ObservableList<Dish> newComboDish = FXCollections.observableArrayList();
            newComboDish.addAll(DishCombo.dishComboList);
            tableComboCreator.getItems().clear();
            tableComboCreator.getItems().addAll(newComboDish);

        } catch (NullPointerException n){

        }
    }

    @FXML
    void saveCombo(ActionEvent event) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.Companion.getConnection();
        int id = Menu.comboList.size() + 1;
        int pTime = 0;
        for (Dish dish: DishCombo.dishComboList) {
            pTime = pTime + dish.getPTime();
        }
        DBConnection.Companion.addCombo(connection, inputComboName.getText(), Integer.valueOf(inputComboPrice.getText()), pTime, 1);
        Menu.comboList.add(new Combo(id, inputComboName.getText(), Integer.valueOf(inputComboPrice.getText()), pTime, 1, new Button("Desactivar")));
        ObservableList<Combo> newCombos = FXCollections.observableArrayList();
        newCombos.addAll(Menu.comboList);
        tableCombos.getItems().clear();
        tableCombos.getItems().addAll(newCombos);
        for (Dish dish: DishCombo.dishComboList) {
            DBConnection.Companion.addComboDish(connection, id, dish.getName());
        }
        new Alert(Alert.AlertType.INFORMATION, "Combo ingresado correctamente en el sistema").showAndWait();
        DishCombo.dishComboList.clear();
    }

    @FXML
    void employeeControl(ActionEvent event) throws IOException {
        WindowBuilder.Companion.createWindow("../views/eRegister.fxml", event, "Registro de empleados");
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

    private void setTableViewMenu() {
        TableColumn<Dish, String> tcName = new TableColumn<Dish, String>("Nombre");
        TableColumn<Dish, String> tcType = new TableColumn<Dish, String>("Tipo");
        TableColumn<Dish, String> tcPrice = new TableColumn<Dish, String>("Precio");
        TableColumn<Dish, String> tcPTime = new TableColumn<Dish, String>("Tiempo de preparacion");
        TableColumn<Dish, Button> tcDisable = new TableColumn<Dish, Button>("");

        tableMenu.getColumns().addAll(tcName, tcType, tcPrice, tcPTime, tcDisable);

        tcName.setCellValueFactory(new PropertyValueFactory<Dish, String>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<Dish, String>("type"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<Dish, String>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<Dish, String>("pTime"));
        tcDisable.setCellValueFactory(new PropertyValueFactory<Dish, Button>("buttonDelete"));
    }

    private void setTableViewComboMenu() {
        TableColumn<Dish, String> tcName = new TableColumn<Dish, String>("Nombre");
        TableColumn<Dish, String> tcType = new TableColumn<Dish, String>("Tipo");

        tableComboCreator.getColumns().addAll(tcName, tcType);

        tcName.setCellValueFactory(new PropertyValueFactory<Dish, String>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<Dish, String>("type"));
    }

    private void setTableViewCombo() {
        TableColumn<Combo, String> tcNumber = new TableColumn<Combo, String>("Numero");
        TableColumn<Combo, String> tcName = new TableColumn<Combo, String>("Nombre");
        TableColumn<Combo, String> tcPrice = new TableColumn<Combo, String>("Precio");
        TableColumn<Combo, String> tcPTime = new TableColumn<Combo, String>("Tiempo de preparacion");
        TableColumn<Combo, Button> tcDisable = new TableColumn<Combo, Button>("");


        tableCombos.getColumns().addAll(tcNumber, tcName, tcPrice, tcPTime, tcDisable);

        tcNumber.setCellValueFactory(new PropertyValueFactory<Combo, String>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<Combo, String>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<Combo, String>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<Combo, String>("pTime"));
        tcDisable.setCellValueFactory(new PropertyValueFactory<Combo, Button>("buttonDelete"));
    }
}
