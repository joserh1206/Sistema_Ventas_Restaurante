package controllers;

import code.DBConnection;
import code.Menu;
import code.Order;
import code.OrderProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML private TableView tableReports;
    @FXML private TableView tableReports1;
    @FXML private Text textTotalSold;
    @FXML private Text textTotalCom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            if (Menu.userName.equals("admin")) {
                int sum = 0;
                Connection connection = DBConnection.Companion.getConnection();
                setTableViewOrder1();
                setTableViewOrder2();
                tableReports.setItems(DBConnection.Companion.getAllBills(connection));
                tableReports1.setItems(DBConnection.Companion.getAllProducts(connection));
                for (Order order :
                        Menu.billList) {
                    sum = sum + order.getTotal();
                }
                textTotalSold.setText(String.valueOf(sum));
                textTotalCom.setText(String.valueOf(Math.round(sum*0.3)));
                connection.close();
            } else if (Menu.userName.equals("admincentral")){
                int sum = 0;
                Connection connection = DBConnection.Companion.getConnection();
                setTableViewOrder1();
                setTableViewOrder2();
                tableReports.setItems(DBConnection.Companion.getAllBillsByOffice(connection, 1));
                tableReports1.setItems(DBConnection.Companion.getAllProductsByOffice(connection, 1));
                for (Order order :
                        Menu.billList) {
                    sum = sum + order.getTotal();
                }
                textTotalSold.setText(String.valueOf(sum));
                textTotalCom.setText(String.valueOf(Math.round(sum*0.3)));
                connection.close();

            } else if (Menu.userName.equals("admincartago")){
                int sum = 0;
                Connection connection = DBConnection.Companion.getConnection();
                setTableViewOrder1();
                setTableViewOrder2();
                tableReports.setItems(DBConnection.Companion.getAllBillsByOffice(connection, 2));
                tableReports1.setItems(DBConnection.Companion.getAllProductsByOffice(connection, 2));
                for (Order order :
                        Menu.billList) {
                    sum = sum + order.getTotal();
                }
                textTotalSold.setText(String.valueOf(sum));
                textTotalCom.setText(String.valueOf(Math.round(sum*0.3)));
                connection.close();
            } else {
                int sum = 0;
                Connection connection = DBConnection.Companion.getConnection();
                setTableViewOrder1();
                setTableViewOrder2();
                tableReports.setItems(DBConnection.Companion.getAllBillsByOffice(connection, 3));
                tableReports1.setItems(DBConnection.Companion.getAllProductsByOffice(connection, 3));
                for (Order order :
                        Menu.billList) {
                    sum = sum + order.getTotal();
                }
                textTotalSold.setText(String.valueOf(sum));
                textTotalCom.setText(String.valueOf(Math.round(sum*0.3)));
                connection.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        WindowBuilder.Companion.createWindow("../views/aMenu.fxml", event, "Panel de control");
    }

    private void setTableViewOrder1() {
        TableColumn<Order, String> tcTotal = new TableColumn<>("Monto");
        TableColumn<Order, String> tcOffice = new TableColumn<>("Oficina");
        TableColumn<Order, String> tcDate = new TableColumn<>("Fecha");

        tableReports.getColumns().addAll(tcTotal, tcOffice, tcDate);
        tableReports.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcTotal.setStyle("-fx-alignment: CENTER;");
        tcOffice.setStyle("-fx-alignment: CENTER;");
        tcDate.setStyle("-fx-alignment: CENTER;");

        tcTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tcOffice.setCellValueFactory(new PropertyValueFactory<>("branchOffice"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void setTableViewOrder2() {
        TableColumn<OrderProduct, String> tcName = new TableColumn<>("Nombre");
        TableColumn<OrderProduct, String> tcTotal = new TableColumn<>("Monto");
        TableColumn<OrderProduct, String> tcOffice = new TableColumn<>("Oficina");
        TableColumn<OrderProduct, String> tcDate = new TableColumn<>("Fecha");

        tableReports1.getColumns().addAll(tcName, tcTotal, tcOffice, tcDate);
        tableReports1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
