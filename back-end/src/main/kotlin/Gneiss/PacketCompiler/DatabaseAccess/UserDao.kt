package Gneiss.PacketCompiler.DatabaseAccess

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class CredentialsResponse(val validFlag: Boolean, val roleId: String)

class UserDao {

    // Username, Password, and URL for the mySQL database
    // Currently using placeholders, will get the actual values from system properties
    var databaseUsername = "user"
    var databasePassword = "password"
    var databaseUrl = "jdbc:mysql://localhost:3306/db"
    var connection: Connection? = null

    // Function to get a connection to the database
    // As we are only using the mySQL database for user management, this connection
    // function only needs to be here, there will be a different one for Redis
    fun getConnection() {
        try {
            this.connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword)
            println("Connected to database")
        } catch (e: SQLException) {
            println("Unable to connect to database")
            e.printStackTrace()
        }
    }

    companion object sqlQueries {
        val getAccountQuery = "SELECT id, email, first_name, last_name FROM users WHERE email = ?"

        val createAccountQuery = "INSERT INTO users (email, password, first_name, last_name, role_id) VALUES (?, ?, ?, ?, ?)"

        val validateCredentialsQuery = "SELECT id, first_name, last_name, role_id FROM users WHERE email = ? AND password = ?"
    }

    fun validateCredentials(email: String, password: String): CredentialsResponse {
        getConnection()
        var prepStatement = this.connection!!.prepareStatement(validateCredentialsQuery)
        prepStatement.setString(1, email)
        prepStatement.setString(2, password)
        var resultSet = prepStatement.executeQuery()

        // Check how many rows are in the result set, if it equal to one it is a valid set of credentials 
        var rowCount = getRowCount(resultSet)
        connection!!.close()

        // If the credentials are valid, we need to get the role_id out of the result
        // To do this, we reset the result set pointer to the first row with beforeFirst() and next()
        // And then get the int in the fourth position, where role_id is the query
        if (rowCount == 1) {
            resultSet.beforeFirst()
            resultSet.next()
            val roleId = resultSet.getInt(4)
            return CredentialsResponse(true, roleId.toString())
        } else {
            return CredentialsResponse(false, (-1).toString())
        }
    }

    fun checkAccountExists(email: String): Boolean {
        getConnection()
        var prepStatement = this.connection!!.prepareStatement(getAccountQuery)
        prepStatement.setString(1, email)
        var resultSet = prepStatement.executeQuery()

        val rowCount = getRowCount(resultSet)
        connection!!.close()
        if (rowCount >= 1) {
            return true
        } else {
            return false
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
