package Gneiss.PacketCompiler.DatabaseAccess

import java.sql.SQLException
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.Connection
import java.sql.ResultSet
import java.lang.IllegalStateException

class UserDao {

    //Username, Password, and URL for the mySQL database
    //Currently using placeholders, will get the actual values from system properties
    var databaseUsername = "user"
    var databasePassword = "password"
    var databaseUrl = "jdbc:mysql://localhost:3306/db"
    var connection: Connection? = null

    //Function to get a connection to the database
    //As we are only using the mySQL database for user management, this connection
    //function only needs to be here, there will be a different one for Redis
    fun getConnection() {
        try {
            connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword)
            println("Connected to database")
        } catch (e: SQLException) {
            println("Unable to connect to database")
            e.printStackTrace()
        }
    }

    companion object sqlQueries {
        val getAccountQuery = "SELECT id, email, firs_tname, last_name FROM users WHERE email = ?"

        val createAccountQuery = "INSERT INTO users (email, password, first_name, last_name, role_id) VALUES (?, ?, ?, ?, ?)"
    }

    fun checkAccountExists(email: String): Boolean {
        getConnection()
        var prepStatement = connection!!.prepareStatement(getAccountQuery)
        prepStatement.setString(1, email)
        var resultSet = prepStatement.executeQuery()

        val rowCount = getRowCount(resultSet)
        connection!!.close()
        if (rowCount >= 1) {
            return true;
        } else {
            return false;
        }
    }

    fun createAccount(email: String, password: String, firstName: String, lastName: String, roleId: Int) {
        getConnection()
        val prepStatement = connection!!.prepareStatement(createAccountQuery)
        prepStatement.setString(1, email)
        prepStatement.setString(2, password)
        prepStatement.setString(3, firstName)
        prepStatement.setString(4, lastName)
        prepStatement.setInt(5, roleId)

        prepStatement.executeUpdate()
        connection!!.close()
    }

    fun getRowCount(resultSet: ResultSet?): Int {
        var size = 0
        while (resultSet!!.next()) {
            size++
        }
        return size
    }
}