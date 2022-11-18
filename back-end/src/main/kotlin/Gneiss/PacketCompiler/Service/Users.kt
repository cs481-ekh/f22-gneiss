package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.IJWTHelper
import Gneiss.PacketCompiler.Models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CreateUserRequest(
    var email: String,
    var password: String,
    var firstName: String,
    var lastName: String
)

class PromoteUserRequest(
    var email: String
)

class DemoteUserRequest(
    var email: String
)

class SetBanUserRequest(
    var email: String,
    var banned: Boolean
)

class GetUsersResponse(
    var users: List<User>
)

class Users(jwtHelper: IJWTHelper, userDao: UserDao) {
    var jwtHelper = jwtHelper
    var userDao = userDao

    fun createUser(req: CreateUserRequest): ResponseEntity<String> {
        // Start by checking if there is already a user with the email specified in the CreateAccountRequest
        // If there is create and return a HTTP error response
        // Assume there is an account, and if not set the flag to false with checkAccountExists()
        var accountExistsFlag: Boolean = userDao.checkAccountExists(req.email)

        if (accountExistsFlag == true) {
            return ResponseEntity<String>("Account already exists for this email", HttpStatus.NOT_ACCEPTABLE)
        } else {
            // Create a new account and return a good response code
            userDao.createAccount(req.email, req.password, req.firstName, req.lastName)
            return ResponseEntity<String>("User account created successfully", HttpStatus.OK)
        }
    }

    fun promoteUser(jwt: String, req: PromoteUserRequest): ResponseEntity<Void> {
        if (jwtHelper.parseJWT(jwt)?.role != "admin") {
            return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
        }
        val res = userDao.promoteUser(req.email)
        if (!res) {
            return ResponseEntity<Void>(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    fun demoteUser(jwt: String, req: DemoteUserRequest): ResponseEntity<Void> {
        if (jwtHelper.parseJWT(jwt)?.role != "admin") {
            return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
        }
        val res = userDao.demoteUser(req.email)
        if (!res) {
            return ResponseEntity<Void>(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    fun setBanUser(jwt: String, req: SetBanUserRequest): ResponseEntity<Void> {
        if (jwtHelper.parseJWT(jwt)?.role != "admin") {
            return ResponseEntity<Void>(HttpStatus.UNAUTHORIZED)
        }
        val res = userDao.setBanUser(req.email, req.banned)
        if (!res) {
            return ResponseEntity<Void>(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    fun getUsers(jwt: String): ResponseEntity<GetUsersResponse> {
        if (jwtHelper.parseJWT(jwt)?.role != "admin") {
            return ResponseEntity<GetUsersResponse>(GetUsersResponse(mutableListOf<User>()), HttpStatus.UNAUTHORIZED)
        }
        return ResponseEntity<GetUsersResponse>(GetUsersResponse(userDao.getUsers()), HttpStatus.OK)
    }
}
