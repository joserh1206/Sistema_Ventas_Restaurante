package code

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException



class DBConnection {
    @Throws(ClassNotFoundException::class, SQLException::class)
    fun getConnection() : Connection {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
        val connectionURL = "jdbc:sqlserver://den1.mssql7.gear.host:1433;databaseName=restaurantedb;user=restaurantedb;password=Bb53z8~~MO3G;"
        println("Connection works!")
        return DriverManager.getConnection(connectionURL)
    }

}