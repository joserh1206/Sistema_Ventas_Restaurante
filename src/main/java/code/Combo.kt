package code

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty


class Combo(id: Int, name: String, price: Int, pTime: Int, state: Int) {

    var listaPedidos: ObservableList<String> = FXCollections.observableArrayList<String>()

    fun setProducts(list : ObservableList<String>){
        listaPedidos = list
    }
}