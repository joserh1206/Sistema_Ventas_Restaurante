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

    @FXML
    private ChoiceBox<String> choiceProductType;
    @FXML
    private TableView tableMenu;
    @FXML
    private TableView tableComboCreator;
    @FXML
    private TableView tableCombos;
    @FXML
    private TextField inputDishName;
    @FXML
    private TextField inputDishPrice;
    @FXML
    private TextField inputPreparationTime;
    @FXML
    private TextField inputComboPrice;
    @FXML
    private TextField inputComboName;

    public void initialize(URL location, ResourceBundle resources) {
        Connection connection;
        try {
            DishCombo.dishComboList.clear();
            connection = DBConnection.Companion.getConnection();
            ResultSet resultSet = DBConnection.Companion.getDishType(connection);
            while (resultSet.next()) {
                choiceProductType.getItems().add(resultSet.getString(1));
            }
            setTableViewMenu();
            setTableViewCombo();
            setTableViewComboMenu();
            tableCombos.setItems(DBConnection.Companion.viewAllCombos(connection));
            tableMenu.setItems(DBConnection.Companion.viewAllDishes(connection));
            connection.close();
            choiceProductType.getSelectionModel().selectFirst();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addDish(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean flag = true;
        String dishName = inputDishName.getText();
        for (Dish dish :
                Menu.dishList) {
            if (dish.getName().equals(dishName)) {
                flag = false;
            }
        }
        if (flag) {
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
            connection.close();
            new Alert(Alert.AlertType.INFORMATION, "Plato ingresado correctamente en el menú").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "El plato ingresado ya existe en el menú").showAndWait();
        }
    }

    void disableDish(Dish dish) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.Companion.getConnection();
        DBConnection.Companion.disableDish(connection, dish.getName());
        connection.close();
        dish.getButtonDelete().setDisable(true);
    }


    @FXML
    void getSelectedCombo(MouseEvent event) throws SQLException, ClassNotFoundException {
        try {
            Combo combo = (Combo) tableCombos.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2 && combo.getButtonDelete().isDisable()) {
                Connection connection = DBConnection.Companion.getConnection();
                DBConnection.Companion.enableCombo(connection, combo.getName());
                combo.getButtonDelete().setDisable(false);
            } else {
                combo.getButtonDelete().setOnAction(e -> {
                    try {
                        disableCombo(combo);
                    } catch (SQLException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                });
            }
        } catch (NullPointerException ignored){

        }
    }

    void disableCombo(Combo combo) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.Companion.getConnection();
        DBConnection.Companion.disableCombo(connection, combo.getName());
        connection.close();
        combo.getButtonDelete().setDisable(true);
    }

    @FXML
    void getSelectedDish(MouseEvent event) throws SQLException, ClassNotFoundException {
        try {
            Dish dish = (Dish) tableMenu.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 3 && dish.getButtonDelete().isDisable()) {
                Connection connection = DBConnection.Companion.getConnection();
                DBConnection.Companion.enableDish(connection, dish.getName());
                dish.getButtonDelete().setDisable(false);
            }
            if (event.getClickCount() == 2 && !dish.getButtonDelete().isDisable()) {
                DishCombo.dishComboList.add(dish);
                tableComboCreator.setItems(DishCombo.dishComboList);
                ObservableList<Dish> newComboDish = FXCollections.observableArrayList();
                newComboDish.addAll(DishCombo.dishComboList);
                tableComboCreator.getItems().clear();
                tableComboCreator.getItems().addAll(newComboDish);
            } else {
                dish.getButtonDelete().setOnAction(e -> {
                    try {
                        disableDish(dish);
                    } catch (SQLException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                });
            }

        } catch (NullPointerException ignored) {

        }
    }

    @FXML
    void deleteDish(MouseEvent event) {
        try {
            Dish dish = (Dish) tableComboCreator.getSelectionModel().getSelectedItem();
            DishCombo.dishComboList.remove(dish);
            tableComboCreator.setItems(DishCombo.dishComboList);
            ObservableList<Dish> newComboDish = FXCollections.observableArrayList();
            newComboDish.addAll(DishCombo.dishComboList);
            tableComboCreator.getItems().clear();
            tableComboCreator.getItems().addAll(newComboDish);
        } catch (NullPointerException ignored) {

        }
    }

    @FXML
    void saveCombo(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean flag = true;
        String comboName = inputComboName.getText();
        for (Combo combo :
                Menu.comboList) {
            if (combo.getName().equals(comboName)) {
                flag = false;
            }
        }
        if (flag) {
            Connection connection = DBConnection.Companion.getConnection();
            int id = Menu.comboList.size() + 1;
            int pTime = 0;
            for (Dish dish : DishCombo.dishComboList) {
                pTime = pTime + dish.getPTime();
            }
            DBConnection.Companion.addCombo(connection, inputComboName.getText(), Integer.valueOf(inputComboPrice.getText()), pTime, 1);
            Menu.comboList.add(new Combo(id, inputComboName.getText(), Integer.valueOf(inputComboPrice.getText()), pTime, 1,
                    new Button("Desactivar")));
            ObservableList<Combo> newCombos = FXCollections.observableArrayList();
            newCombos.addAll(Menu.comboList);
            tableCombos.getItems().clear();
            tableCombos.getItems().addAll(newCombos);
            for (Dish dish : DishCombo.dishComboList) {
                DBConnection.Companion.addComboDish(connection, id, dish.getName());
            }
            new Alert(Alert.AlertType.INFORMATION, "Combo ingresado correctamente en el menú").showAndWait();
            DishCombo.dishComboList.clear();
            connection.close();
        } else {
            new Alert(Alert.AlertType.ERROR, "El combo ingresado ya se enceuntra en el menú").showAndWait();
        }
    }

    @FXML
    void openReports(ActionEvent event) throws IOException {
        WindowBuilder.Companion.createWindow("../views/reports.fxml", event, "Reportes");
    }

    @FXML
    void employeeControl(ActionEvent event) throws IOException {
        WindowBuilder.Companion.createWindow("../views/eRegister.fxml", event, "Registro de empleados");
    }


    @FXML
    void goBack(ActionEvent event) throws IOException {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Esta seguro que desea salir?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();
        if (confirmation.getResult() == ButtonType.YES) {
            WindowBuilder.Companion.createWindow("../views/login.fxml", event, "Iniciar sesión");
        } else {
            confirmation.close();
        }
    }

    private void setTableViewMenu() {
        TableColumn<Dish, String> tcName = new TableColumn<>("Nombre");
        TableColumn<Dish, String> tcType = new TableColumn<>("Tipo");
        TableColumn<Dish, String> tcPrice = new TableColumn<>("Precio");
        TableColumn<Dish, String> tcPTime = new TableColumn<>("Tiempo de preparacion");
        TableColumn<Dish, Button> tcDisable = new TableColumn<>("");

        tableMenu.getColumns().addAll(tcName, tcType, tcPrice, tcPTime, tcDisable);
        tableMenu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcName.setStyle("-fx-alignment: CENTER;");
        tcType.setStyle("-fx-alignment: CENTER;");
        tcPrice.setStyle("-fx-alignment: CENTER;");
        tcPTime.setStyle("-fx-alignment: CENTER;");

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<>("pTime"));
        tcDisable.setCellValueFactory(new PropertyValueFactory<>("buttonDelete"));
    }

    private void setTableViewComboMenu() {
        TableColumn<Dish, String> tcName = new TableColumn<>("Nombre");
        TableColumn<Dish, String> tcType = new TableColumn<>("Tipo");

        tableComboCreator.getColumns().addAll(tcName, tcType);
        tableComboCreator.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcName.setStyle("-fx-alignment: CENTER;");
        tcType.setStyle("-fx-alignment: CENTER;");

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    private void setTableViewCombo() {
        TableColumn<Combo, String> tcNumber = new TableColumn<>("N°");
        TableColumn<Combo, String> tcName = new TableColumn<>("Nombre");
        TableColumn<Combo, String> tcPrice = new TableColumn<>("Precio");
        TableColumn<Combo, String> tcPTime = new TableColumn<>("Tiempo de preparacion");
        TableColumn<Combo, Button> tcDisable = new TableColumn<>("");


        tableCombos.getColumns().addAll(tcNumber, tcName, tcPrice, tcPTime, tcDisable);
        tableCombos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcNumber.setStyle("-fx-alignment: CENTER;");
        tcName.setStyle("-fx-alignment: CENTER;");
        tcPrice.setStyle("-fx-alignment: CENTER;");
        tcPTime.setStyle("-fx-alignment: CENTER;");

        tcNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcPTime.setCellValueFactory(new PropertyValueFactory<>("pTime"));
        tcDisable.setCellValueFactory(new PropertyValueFactory<>("buttonDelete"));
    }
}
