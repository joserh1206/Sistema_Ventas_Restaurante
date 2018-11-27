package code;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Menu {
    public static String userName;
    public static ObservableList<Dish> dishList = FXCollections.observableArrayList();
    public static ObservableList<Combo> comboList = FXCollections.observableArrayList();
    public static ObservableList<Object> orderList = FXCollections.observableArrayList();
    public static ObservableList<Order> billList = FXCollections.observableArrayList();
    public static ObservableList<OrderProduct> billDetailList = FXCollections.observableArrayList();
}
