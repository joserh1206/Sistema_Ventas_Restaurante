package controllers;

import code.DBConnection;
import code.Employee;
import code.Payroll;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ERegisterController implements Initializable {

    @FXML private TableView tablePayroll;
    @FXML private ComboBox<String> selectRole;
    @FXML private ComboBox<String> selectOffice;
    @FXML private Slider salaryRange;
    @FXML private Text labelSalary;
    @FXML private TextField inputName;

    private ArrayList<Integer> minSalary = new ArrayList<Integer>();
    private ArrayList<Integer> maxSalary = new ArrayList<Integer>();
    private ArrayList<String> role = new ArrayList<String>();

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection connection = DBConnection.Companion.getConnection();

            setTableViewEmployee();
            tablePayroll.setItems(DBConnection.Companion.viewEmployees(connection));
            ResultSet resultSet = DBConnection.Companion.getEmployeeRole(connection);
            while (resultSet.next()){
                selectRole.getItems().add(resultSet.getString(1));
                role.add(resultSet.getString(1));
                minSalary.add(resultSet.getInt(2));
                maxSalary.add(resultSet.getInt(3));
            }
            resultSet = DBConnection.Companion.getBranchOffice(connection);

            while (resultSet.next()){
                selectOffice.getItems().add(resultSet.getString(1));
            }
            selectRole.getSelectionModel().selectFirst();
            selectOffice.getSelectionModel().selectFirst();
            salaryRange.setMin(minSalary.get(0));
            salaryRange.setMax(maxSalary.get(0));
            salaryRange.setBlockIncrement(1000);
            salaryRange.setShowTickLabels(false);
            salaryRange.setShowTickMarks(false);
            labelSalary.setText(String.valueOf(minSalary.get(0)));

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateLabel(MouseEvent event) {
        labelSalary.setText(String.valueOf(Math.round(salaryRange.getValue())));
    }

    @FXML
    void updateSlider(ActionEvent event) {
        int index = role.indexOf(selectRole.getValue());
        salaryRange.setValue(minSalary.get(index));
        labelSalary.setText(String.valueOf(minSalary.get(index)));
        salaryRange.setMin(minSalary.get(index));
        salaryRange.setMax(maxSalary.get(index));
    }

    @FXML
    void saveEmployee(ActionEvent event) throws SQLException, ClassNotFoundException {
//        WindowBuilder.Companion.createWindow("../views/aMenu.fxml", event, "Panel de control");
        Connection connection = DBConnection.Companion.getConnection();
        DBConnection.Companion.addEmployee(connection, inputName.getText(), selectRole.getValue(),
                (int)Math.round(salaryRange.getValue()), selectOffice.getValue());
        Payroll.employeeList.add(new Employee(inputName.getText(), selectRole.getValue(),
                (int)Math.round(salaryRange.getValue()), selectOffice.getValue()));
        ObservableList<Employee> newEmployeeList = FXCollections.observableArrayList();
        newEmployeeList.addAll(Payroll.employeeList);
        tablePayroll.getItems().clear();
        tablePayroll.getItems().addAll(newEmployeeList);
        new Alert(Alert.AlertType.INFORMATION, "Empleado ingresado correctamente en el sistema").showAndWait();
    }



    @FXML
    void goBack(ActionEvent event) throws IOException {
        WindowBuilder.Companion.createWindow("../views/aMenu.fxml", event, "Panel de control");
    }


    private void setTableViewEmployee() {
        TableColumn<Employee, String> tcName = new TableColumn<Employee, String>("Nombre");
        TableColumn<Employee, String> tcRole = new TableColumn<Employee, String>("Rol");
        TableColumn<Employee, String> tcSalary = new TableColumn<Employee, String>("Salario");
        TableColumn<Employee, String> tcOffice = new TableColumn<Employee, String>("Sucursal");

        tablePayroll.getColumns().addAll(tcName, tcRole, tcSalary, tcOffice);

        tcName.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
        tcRole.setCellValueFactory(new PropertyValueFactory<Employee, String>("role"));
        tcSalary.setCellValueFactory(new PropertyValueFactory<Employee, String>("salary"));
        tcOffice.setCellValueFactory(new PropertyValueFactory<Employee, String>("office"));
    }

}
