package code

import code.Menu.comboList
import code.Menu.dishList
import code.Payroll.employeeList
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import javafx.collections.ObservableList
import javafx.scene.control.Button


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
            Menu.comboList.clear()
            val query = "{ call viewCombosSP() }"
            val call = con.prepareCall(query)
            var resultSet = call.executeQuery()
            while (resultSet.next()) {
                var id = resultSet.getInt(1)
                var name = resultSet.getString(2)
                var price = resultSet.getInt(3)
                var preparationTime = resultSet.getInt(4)
                var state = resultSet.getInt(5)
                var button = Button("Desactivar")
                var combo = Combo(id, name, price, preparationTime, state, button)
                comboList.add(combo)
            }
            return comboList
        }

        @Throws(SQLException::class)
        fun viewAllCombos(con: Connection): ObservableList<Combo> {
            Menu.comboList.clear()
            val query = "{ call viewAllCombosSP() }"
            val call = con.prepareCall(query)
            var resultSet = call.executeQuery()
            while (resultSet.next()) {
                var id = resultSet.getInt(1)
                var name = resultSet.getString(2)
                var price = resultSet.getInt(3)
                var preparationTime = resultSet.getInt(4)
                var state = resultSet.getInt(5)
                var button = Button("Desactivar")
                var combo = Combo(id, name, price, preparationTime, state, button)
                comboList.add(combo)
            }
            return comboList
        }

        @Throws(SQLException::class)
        fun viewDishes(con: Connection): ObservableList<Dish> {
            Menu.dishList.clear()
            val query = "{ call viewDishSP() }"
            val call = con.prepareCall(query)
            var resultSet = call.executeQuery()
            while (resultSet.next()) {
                var name = resultSet.getString(1)
                var type = resultSet.getString(2)
                var price = resultSet.getInt(3)
                var preparationTime = resultSet.getInt(4)
                var state = resultSet.getInt(5)
                var button = Button("Desactivar")
                var dish = Dish(name, type, price, preparationTime, state, button)
                dishList.add(dish)
            }
            return dishList
        }

        @Throws(SQLException::class)
        fun viewAllDishes(con: Connection): ObservableList<Dish> {
            dishList.clear()
            val query = "{ call viewAllDishSP() }"
            val call = con.prepareCall(query)
            var resultSet = call.executeQuery()
            while (resultSet.next()) {
                var name = resultSet.getString(1)
                var type = resultSet.getString(2)
                var price = resultSet.getInt(3)
                var preparationTime = resultSet.getInt(4)
                var state = resultSet.getInt(5)
                var button = Button("Desactivar")
                var dish = Dish(name, type, price, preparationTime, state, button)
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
            employeeList.clear()
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

        @Throws(SQLException::class)
        fun addDish(con: Connection, name: String, price:Int, pTime: Int, type: String){
            val query = "{ call addIntoMenuSP(?,?,?,?) }"
            val call = con.prepareCall(query)
            call.setString(1, name)
            call.setInt(2, price)
            call.setInt(3, pTime)
            call.setString(4, type)
            call.execute()
        }

        @Throws(SQLException::class)
        fun addCombo(con: Connection, name: String, price: Int, pTime: Int, state: Int){
            val query = "{ call addCombo(?,?,?,?) }"
            val call = con.prepareCall(query)
            call.setString(1, name)
            call.setInt(2, price)
            call.setInt(3, pTime)
            call.setInt(4, state)
            call.execute()
        }

        @Throws(SQLException::class)
        fun addComboDish(con: Connection, comboID: Int, dishName: String){
            val query = "{ call addComboDish(?,?) }"
            val call = con.prepareCall(query)
            call.setInt(1, comboID)
            call.setString(2, dishName)
            call.execute()
        }

        @Throws(SQLException::class)
        fun addEmployee(con: Connection, name: String, role:String, salary: Int, office: String){
            val query = "{ call employeeRegistrationSP(?,?,?,?) }"
            val call = con.prepareCall(query)
            call.setString(1, name)
            call.setString(2, role)
            call.setInt(3, salary)
            call.setString(4, office)
            call.execute()
        }


    }




}