package controllers

import code.DBConnection
import code.Menu
import javafx.event.ActionEvent
import javafx.fxml.Initializable
import java.net.URL
import java.util.*
import javafx.fxml.FXML
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import java.sql.Connection
import javafx.scene.control.Alert


class LoginController : Initializable {


    @FXML
    private var inputUserName : TextField = TextField()

    @FXML
    private var inputPassword : PasswordField = PasswordField()

    @FXML
    fun getIn(event : ActionEvent) {
//        WindowBuilder.createWindow("../views/cMenu.fxml", event, "Sistema de pedidos")
        val dbConnection = DBConnection
        val connection = dbConnection.getConnection()
//        val statement : Statement = connection.createStatement()
//        val resultSet = statement.executeQuery("select * from pruebaLogin") // This will be changed for SP

        val resultSet = dbConnection.getUserAccess(connection, inputUserName.text, inputPassword.text)
        connection.close()

//        while (resultSet.next()){
//            val usuario : String = resultSet.getString(1)
//            val contrasenia : String = resultSet.getString(2)
//            println("$usuario $contrasenia")
//        }
        when (resultSet) {
            1 ->
            {
                WindowBuilder.createWindow("../views/cMenu.fxml", event, "Restaurante Online")
                Menu.userName = inputUserName.text
//                Usuario.usuarioActual = txtUsuario.getText()
            }
            2 ->
            {
                WindowBuilder.createWindow("../views/aMenu.fxml", event, "Panel de control")
                Menu.userName = inputUserName.text
//                Usuario.usuarioActual = txtUsuario.getText()
            }
            0 -> Alert(Alert.AlertType.ERROR, "La contraseña es incorrecta, intente de nuevo").showAndWait()
            else -> Alert(Alert.AlertType.ERROR, "El usuario ingresado no existe, intente de nuevo").showAndWait()
        }
    }

    @FXML
    fun openRegistration(event : ActionEvent) {
        WindowBuilder.createWindow("../views/cRegister.fxml", event, "Registro de usuario")
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }
}