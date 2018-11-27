package controllers;

import code.DBConnection;
import code.OrderProduct;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class myOrdersController implements Initializable {
    @FXML private TableView tableOrders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection connection = DBConnection.Companion.getConnection();
            setTableViewOrder();
            tableOrders.setItems(DBConnection.Companion.getOrdersByClient(connection));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setTableViewOrder() {
        TableColumn<OrderProduct, String> tcName = new TableColumn<>("Nombre");
        TableColumn<OrderProduct, String> tcTotal = new TableColumn<>("Monto");
        TableColumn<OrderProduct, String> tcOffice = new TableColumn<>("Oficina");
        TableColumn<OrderProduct, String> tcDate = new TableColumn<>("Fecha");

        tableOrders.getColumns().addAll(tcName, tcTotal, tcOffice, tcDate);
        tableOrders.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcTotal.setStyle("-fx-alignment: CENTER;");
        tcOffice.setStyle("-fx-alignment: CENTER;");
        tcDate.setStyle("-fx-alignment: CENTER;");
        tcName.setStyle("-fx-alignment: CENTER;");

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tcOffice.setCellValueFactory(new PropertyValueFactory<>("branchOffice"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

}
