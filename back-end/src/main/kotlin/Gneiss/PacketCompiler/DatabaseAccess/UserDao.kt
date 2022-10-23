package Gneiss.PacketCompiler.DatabaseAccess

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class CredentialsResponse(val validFlag: Boolean, val roleId: String)

class UserDao {

    var databaseUsername = System.getenv("MYSQL_USER")
    var databasePassword = System.getenv("MYSQL_PASSWORD")
    var databaseUrl = "jdbc:mysql://${System.getenv("MYSQL_HOST")}:${System.getenv("MYSQL_PORT")}/${System.getenv("MYSQL_DATABASE")}"
    var connection: Connection? = null

    // Function to get a connection to the database
    // As we are only using the mySQL database for user management, this connection
    // function only needs to be here, there will be a different one for Redis
    fun getConnection() {
        try {
            connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    companion object sqlQueries {
        val getAccountQuery = "SELECT id, email, first_name, last_name FROM users WHERE email = ?"

        val createAccountQuery = "INSERT INTO users (email, password, first_name, last_name, role_id) VALUES (?, ?, ?, ?, 'user')"

        val validateCredentialsQuery = "SELECT id, first_name, last_name, role_id FROM users WHERE email = ? AND password = ?"
    }

    fun validateCredentials(email: String, password: String): CredentialsResponse {
        getConnection()
        var prepStatement = connection!!.prepareStatement(validateCredentialsQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
        prepStatement.setString(1, email)
        prepStatement.setString(2, password)
        var resultSet = prepStatement.executeQuery()

        // Check how many rows are in the result set, if it equal to one it is a valid set of credentials
        var rowCount = getRowCount(resultSet)

        // If the credentials are valid, we need to get the role_id out of the result
        // To do this, we reset the result set pointer to the first row with beforeFirst() and next()
        // And then get the int in the fourth position, where role_id is the query
        if (rowCount == 1) {
            resultSet.beforeFirst()
            resultSet.next()
            val roleId = resultSet.getString(4)
            connection!!.close()
            return CredentialsResponse(true, roleId)
        } else {
            connection!!.close()
            return CredentialsResponse(false, "invalidRole")
        }
    }

    fun checkAccountExists(email: String): Boolean {
        getConnection()
        var prepStatement = connection!!.prepareStatement(getAccountQuery)
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

    fun createAccount(email: String, password: String, firstName: String, lastName: String) {
        getConnection()
        val prepStatement = connection!!.prepareStatement(createAccountQuery)
        prepStatement.setString(1, email)
        prepStatement.setString(2, password)
        prepStatement.setString(3, firstName)
        prepStatement.setString(4, lastName)

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
