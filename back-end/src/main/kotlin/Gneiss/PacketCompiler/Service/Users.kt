package Gneiss.PacketCompiler.Service


import org.springframework.http.*
import java.lang.IllegalStateException

data class CreateAccountRequest(
        var email: String,
        var password: String,
        var firstName: String,
        var lastName: String,
        var roleId: Int,
)

class Users {
    companion object {
        fun createAccount(req: CreateAccountRequest): ResponseEntity<String> {
            //Start by checking if there is already a user with the email specified in the CreateAccountRequest
            //If there is create and return a HTTP error response
            //Assume there is an account, and if not set the flag to false with checkAccountExists()
            var accountExistsFlag: Boolean = true
            try {
                accountExistsFlag = UserDao.checkAccountExists()
            } catch (e: IllegalStateException) {
                println(e.getMessage())
                return ResponseEntity<>("Multiple accounts linked to this email", HttpStatus.BAD_REQUEST)
            }

            if(accountExistsFlag == true) {
                return ResponseEntity<>("Account already exists for this email", HttpStatus.BAD_REQUEST)
            } else {
                //Create a new account and return a good response code
                UserDao.createAccount(req.email, req.password, req.firstName, req.lastName, req.roleId)
                return ResponseEntity<>("Account created successfully", HttpStatus.OK)
            }
        }
    }
}