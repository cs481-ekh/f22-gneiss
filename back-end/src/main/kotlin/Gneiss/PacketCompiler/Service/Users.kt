package Gneiss.PacketCompiler.Service


import org.springframework.http.*
import java.lang.IllegalStateException

class CreateUserRequest(
        var email: String,
        var password: String,
        var firstName: String,
        var lastName: String,
        var roleId: Int,
)

class CreateUserResponse(
        var responseString: String
)

class Users {
    companion object {
        fun createUser(req: CreateUserRequest): CreateUserResponse {
            //Start by checking if there is already a user with the email specified in the CreateAccountRequest
            //If there is create and return a HTTP error response
            //Assume there is an account, and if not set the flag to false with checkAccountExists()
            var accountExistsFlag: Boolean = true
            try {
                accountExistsFlag = UserDao.checkAccountExists()
            } catch (e: IllegalStateException) {
                println(e.getMessage())
                return CreateUserResponse("Multiple accounts linked to this email")
            }

            if(accountExistsFlag == true) {
                return CreateUserResponse("Account already exists for this email")
            } else {
                //Create a new account and return a good response code
                UserDao.createAccount(req.email, req.password, req.firstName, req.lastName, req.roleId)
                return CreateUserResponse("User account created successfully")
            }
        }
    }
}