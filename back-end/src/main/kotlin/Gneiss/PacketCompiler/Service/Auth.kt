package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.IJWTHelper
import Gneiss.PacketCompiler.Helpers.JWTBody
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class AuthResponse(val validJWT: Boolean)

class Auth(jwtHelper: IJWTHelper, userDao: UserDao) {
    var jwtHelper = jwtHelper
    var userDao = userDao

    fun authenticateJWT(jwt: String): ResponseEntity<AuthResponse> {
        if (jwt.equals("")) {
            return ResponseEntity<AuthResponse>(AuthResponse(false), HttpStatus.UNAUTHORIZED)
        }

        val jwtBody: JWTBody? = jwtHelper.parseJWT(jwt)

        var validFlag = false
        var response = AuthResponse(validFlag)
        // Use the credentials from the jwtBody to query the database and confirm the email/role exists
        if (jwtBody == null) {
            return ResponseEntity<AuthResponse>(response, HttpStatus.UNAUTHORIZED)
        } else {
            validFlag = userDao.checkAccountExists(jwtBody.user)
            response = AuthResponse(validFlag)
        }

        // If so return true inside an AuthResponse
        if (validFlag) {
            return ResponseEntity<AuthResponse>(response, HttpStatus.OK)
        } else { // Else return false inside an AuthResponse
            return ResponseEntity<AuthResponse>(response, HttpStatus.UNAUTHORIZED)
        }
    }
}
