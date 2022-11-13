package Gneiss.PacketCompiler.DatabaseAccess

import org.springframework.security.crypto.bcrypt.BCrypt
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import Gneiss.PacketCompiler.Models.User

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

        val validateCredentialsQuery = "SELECT id, password, first_name, last_name, role_id FROM users WHERE email = ?"

        val getUsersQuery = "SELECT email, role_id, banned FROM users"

        val promoteUserQuery = "UPDATE users SET role_id = 'admin' WHERE email = ?"

        val demoteUserQuery = "UPDATE users SET role_id = 'user' WHERE email = ?"

        val setBanUserQuery = "UPDATE users SET banned = ? WHERE email = ?"
    }

    fun validateCredentials(email: String, password: String): CredentialsResponse {
        getConnection()
        var prepStatement = connection!!.prepareStatement(validateCredentialsQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
        prepStatement.setString(1, email)
        var resultSet = prepStatement.executeQuery()
        resultSet.next()
        val hashedPassword = resultSet.getString(2)
        val roleId = resultSet.getString(5)
        connection!!.close()
        if (BCrypt.checkpw(password, hashedPassword)) {
            return CredentialsResponse(true, roleId)
        }
        return CredentialsResponse(false, "invalidRole")
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
        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
        prepStatement.setString(1, email)
        prepStatement.setString(2, passwordHash)
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

    fun getUsers(): List<User> {
        getConnection()
        val ret = mutableListOf<User>()
        connection.use {
            val prepStatement = connection!!.prepareStatement(getUsersQuery)
            var resultSet = prepStatement.executeQuery()
            while (resultSet!!.next()) {
                ret.add(User(resultSet.getString(1), resultSet.getString(2), resultSet.getBoolean(3)))
            }
        }
        return ret
    }

    fun promoteUser(userEmail: String): Boolean {
        if (!checkAccountExists(userEmail)) {
            return false
        }

        getConnection()
        connection.use {
            val prepStatement = connection!!.prepareStatement(promoteUserQuery)
            prepStatement.setString(1, userEmail)
            prepStatement.executeUpdate()
        }
        return true
    }

    fun demoteUser(userEmail: String): Boolean {
        if (!checkAccountExists(userEmail)) {
            return false
        }

        getConnection()
        connection.use {
            val prepStatement = connection!!.prepareStatement(demoteUserQuery)
            prepStatement.setString(1, userEmail)
            prepStatement.executeUpdate()
        }
        return true
    }
    
    fun setBanUser(userEmail: String, banned: Boolean): Boolean {
        if (!checkAccountExists(userEmail)) {
            return false
        }

        getConnection()
        connection.use {
            val prepStatement = connection!!.prepareStatement(setBanUserQuery)
            prepStatement.setBoolean(1, banned)
            prepStatement.setString(2, userEmail)
            prepStatement.executeUpdate()
        }
        return true
    }
}
