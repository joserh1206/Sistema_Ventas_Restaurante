package controllers

import javafx.event.ActionEvent
import javafx.fxml.Initializable
import java.net.URL
import java.util.*
import javafx.fxml.FXML
import javafx.scene.control.TextField


class ERegisterController : Initializable {


    @FXML
    private var inputName : TextField = TextField() //Remember to declare "var", no "val" variables

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

    @FXML
    fun saveEmployee(event : ActionEvent) {
        println(inputName.text + " Dont know")
    }
}