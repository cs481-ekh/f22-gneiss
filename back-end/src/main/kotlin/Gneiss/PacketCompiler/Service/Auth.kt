package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.JWTBody
import Gneiss.PacketCompiler.Helpers.JWTHelper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class AuthRequest(val jwt: String)

class AuthResponse(val validJWT: Boolean)

class Auth(jwtHelper: JWTHelper, userDao: UserDao) {
    var jwtHelper = jwtHelper
    var userDao = userDao
 
    fun authenticateJWT(req: AuthRequest): ResponseEntity<AuthResponse> {
        //Parse the JWT passed in into the body
        val jwtBody: JWTBody? = jwtHelper.parseJWT(req.jwt)

        //Use the credentials from the jwtBody to query the database and confirm the email/role exists
        val validFlag = userDao.checkAccountExists(jwtBody.user)
        val response = AuthResponse(validFlag)
        
        //If so return true inside an AuthResponse
        if (validFlag) {
            return ResponseEntity<AuthResponse>(response, HttpStatus.OK)
        } else { //Else return false inside an AuthResponse
            return ResponseEntity<AuthResponse>(response, HttpStatus.UNAUTHORIZED)
        }
    }
}