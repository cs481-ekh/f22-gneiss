package Gneiss.PacketCompiler.Service

//Potential library for HTTP responses
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import org.w3c.fetch.ResponseType

import java.lang.IllegalStateException

data class CreateAccountRequest(
        var email: String,
        var password: String,
        var firstName: String,
        var lastName: String
)
class CreateAccountResponse()

class Users {
    companion object {
        fun createAccount(req: CreateAccountRequest): CreateAccountResponse {
            //Start by checking if there is already a user with the email specified in the CreateAccountRequest
            //If there is create and return a HTTP error response
            //Assume there is an account, and if not set the flag to false with checkAccountExists()
            var accountExistsFlag: Boolean = true
            try {
                accountExistsFlag = UserDao.checkAccountExists()
            } catch (e: IllegalStateException) {
                println(e.getMessage())
                //Create some error HTTP response and return it

            }

            if(accountExistsFlag == true) {

            } else {

            }

            return CreateAccountResponse()
        }
    }
}