package controllers

import code.DBConnection
import javafx.event.ActionEvent
import javafx.fxml.Initializable
import java.net.URL
import java.util.*
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.TextArea
import javafx.scene.control.TextField


class CRegisterController : Initializable{

    @FXML
    private var inputName: TextField = TextField()

    @FXML
    private var inputUserName: TextField = TextField()

    @FXML
    private var inputPassword: TextField = TextField()

    @FXML
    private var inputPhone: TextField = TextField()

    @FXML
    private var inputAddress: TextArea = TextArea()


    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

    @FXML
    fun registerClient(event: ActionEvent) {
        val dbConnection = DBConnection
        val connection = dbConnection.getConnection()
        val resultSet = dbConnection.addClient(connection, inputName.text, inputUserName.text,
                                        inputPassword.text, inputPhone.text, inputAddress.text)
        connection.close()

        when (resultSet) {
            1 -> {
                Alert(Alert.AlertType.INFORMATION, "El usuario se ha creado con éxito").showAndWait()
                WindowBuilder.createWindow("../views/cMenu.fxml", event, "Restaurante Online")
            }
            0 -> Alert(Alert.AlertType.ERROR,
                    "El nombre de usuario ya existe en la base de datos").showAndWait()
            else -> Alert(Alert.AlertType.ERROR,
                    "Ha ocurrido un error al crear el usuario, intente de nuevo").showAndWait()
        }
    }


    @FXML
    fun goback(event: ActionEvent) {
        WindowBuilder.createWindow("../views/login.fxml", event, "Lniciar sesión")
    }
}