package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.CredentialsResponse
import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.JWTBody
import Gneiss.PacketCompiler.Helpers.JWTHelper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class LoginRequest(val username: String, val password: String)

class Login(jwtHelper: JWTHelper, userDao: UserDao) {
    var jwtHelper = jwtHelper
    var userDao = userDao

    fun login(req: LoginRequest): ResponseEntity<String> {
        //Check if the credentials passed in exist in the database table 'Users'
        var validCredentials: CredentialsResponse = userDao.validateCredentials(req.username, req.password)

        //If validCredentials is false, return a 401 response 'authentication failed'
        //Otherwise return a 200 response with a jwt token as the body
        if (!validCredentials.validFlag) {
            return ResponseEntity<String>("Invalid Credentials", HttpStatus.UNAUTHORIZED)
        } else {
            val jwt = jwtHelper.createJWT(JWTBody(req.username, validCredentials.roleId))
            return ResponseEntity<String>(jwt, HttpStatus.OK)
        }
    }
}
