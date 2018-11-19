package controllers

import javafx.event.ActionEvent
import javafx.fxml.Initializable
import java.net.URL
import java.util.*
import javafx.fxml.FXML
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField


class LoginController : Initializable {


    @FXML
    private var inputUserName : TextField = TextField()

    @FXML
    private var inputPassword : PasswordField = PasswordField()

    @FXML
    fun getIn(event : ActionEvent) {
        WindowBuilder.crearVentana("../views/cMenu.fxml", event, "Sistema de pedidos")
    }

    @FXML
    fun openRegistration(event : ActionEvent) {

    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }
}