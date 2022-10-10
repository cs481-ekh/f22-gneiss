package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.Helpers.JWTBody
import Gneiss.PacketCompiler.Helpers.JWTHelper

class LoginRequest(val username: String, val password: String)
class LoginResponse(val jwt: String?)

class Login(jwtHelper: JWTHelper) {
    var jwtHelper = jwtHelper

    fun login(req: LoginRequest): LoginResponse {
        return LoginResponse(jwt = jwtHelper.createJWT(JWTBody(req.username, "admin"))) // values hard-coded for demo purposes
    }
}
