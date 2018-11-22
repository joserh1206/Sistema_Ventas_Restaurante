package code

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import javafx.collections.FXCollections
import javafx.collections.ObservableList




class DBConnection {
    companion object {
        @Throws(ClassNotFoundException::class, SQLException::class)
        fun getConnection(): Connection {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            val connectionURL = "jdbc:sqlserver://den1.mssql7.gear.host:1433;databaseName=restaurantedb;user=restaurantedb;password=Bb53z8~~MO3G;"
            return DriverManager.getConnection(connectionURL)
        }

        @Throws(SQLException::class)
        fun getUserAccess(con: Connection, userName: String, password: String): Int {
            var userType = -1

            val query = "{ call loginSP(?,?) }"
            val call = con.prepareCall(query)
            call.setString(1, userName)
            call.setString(2, password)
            val rs = call.executeQuery()
            while (rs.next()) {
                userType = rs.getInt(1)
            }
            return userType
        }

        @Throws(SQLException::class)
        fun addClient(con: Connection, name: String, userName: String, password: String, phoneNumber: String, address: String): Int {
            var statusVar: Int
            val query = "{ call clientRegistrationSP(?,?,?,?,?) }"
            val call = con.prepareCall(query)
            call.setString(1, name)
            call.setString(2, userName)
            call.setString(3, password)
            call.setString(4, phoneNumber)
            call.setString(5, address)
            statusVar = try {
                call.executeUpdate()
            } catch (e: Exception) {
                0
            }
            return statusVar
        }

        @Throws(SQLException::class)
        fun getDishType(con: Connection): ResultSet {
            val query = "{ call getDishType() }"
            val call = con.prepareCall(query)
            return call.executeQuery()
        }

        @Throws(SQLException::class)
        fun viewCombos(con: Connection): ObservableList<Combo> {
            val comboList = FXCollections.observableArrayList<Combo>()
            val query = "{ call viewCombosSP() }"
            val call = con.prepareCall(query)
            var resultSet = call.executeQuery()
            while (resultSet.next()) {
                var id = resultSet.getInt(1)
                var name = resultSet.getString(2)
                var price = resultSet.getInt(3)
                var preparationTime = resultSet.getInt(4)
                var state = resultSet.getInt(5)
                var combo = Combo(id, name, price, preparationTime, state)
                comboList.add(combo)
            }
            return comboList
        }

        @Throws(SQLException::class)
        fun viewDishes(con: Connection): ObservableList<Dish> {
            val dishList = FXCollections.observableArrayList<Dish>()
            val query = "{ call viewDishSP() }"
            val call = con.prepareCall(query)
            var resultSet = call.executeQuery()
            while (resultSet.next()) {
                var name = resultSet.getString(1)
                var type = resultSet.getString(2)
                var price = resultSet.getInt(3)
                var preparationTime = resultSet.getInt(4)
                var state = resultSet.getInt(5)
                var dish = Dish(name, type, price, preparationTime, state)
                dishList.add(dish)
            }
            return dishList
        }

        @Throws(SQLException::class)
        fun getBranchOffice(con: Connection): ResultSet {
            val query = "{ call viewBranchOffice() }"
            val call = con.prepareCall(query)
            return call.executeQuery()
        }

        @Throws(SQLException::class)
        fun viewEmployees(con: Connection): ObservableList<Employee> {
            val employeeList = FXCollections.observableArrayList<Employee>()
            val query = "{ call viewEmployeeSP() }"
            val call = con.prepareCall(query)
            var resultSet = call.executeQuery()
            while (resultSet.next()) {
                var name = resultSet.getString(1)
                var role = resultSet.getString(2)
                var salary = resultSet.getInt(3)
                var office = resultSet.getString(4)
                var employee = Employee(name, role, salary, office)
                employeeList.add(employee)
            }
            return employeeList
        }

        @Throws(SQLException::class)
        fun getEmployeeRole(con: Connection): ResultSet {
            val query = "{ call getEmployeeRoleSP() }"
            val call = con.prepareCall(query)
            return call.executeQuery()
        }


    }




}