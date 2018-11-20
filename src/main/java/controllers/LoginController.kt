package controllers

import code.DBConnection
import javafx.event.ActionEvent
import javafx.fxml.Initializable
import java.net.URL
import java.util.*
import javafx.fxml.FXML
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import java.sql.Connection
import java.sql.Statement


class LoginController : Initializable {


    @FXML
    private var inputUserName : TextField = TextField()

    @FXML
    private var inputPassword : PasswordField = PasswordField()

    @FXML
    fun getIn(event : ActionEvent) {
//        WindowBuilder.crearVentana("../views/cMenu.fxml", event, "Sistema de pedidos")
        val dbConnection = DBConnection()
        val connection : Connection = dbConnection.getConnection()
        val statement : Statement = connection.createStatement()
        val resultSet = statement.executeQuery("select * from pruebaLogin") // This will be changed for SP

        while (resultSet.next()){
            val usuario : String = resultSet.getString(1)
            val contrasenia : String = resultSet.getString(2)
            println("$usuario $contrasenia")
        }
    }

    @FXML
    fun openRegistration(event : ActionEvent) {

    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }
}