package Gneiss.PacketCompiler.Controller

import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.JWTHelper
import Gneiss.PacketCompiler.Service.CreateTestRequest
import Gneiss.PacketCompiler.Service.CreateTestResponse
import Gneiss.PacketCompiler.Service.CreateUserRequest
import Gneiss.PacketCompiler.Service.Login
import Gneiss.PacketCompiler.Service.LoginRequest
import Gneiss.PacketCompiler.Service.LoginResponse
import Gneiss.PacketCompiler.Service.Auth
import Gneiss.PacketCompiler.Service.AuthRequest
import Gneiss.PacketCompiler.Service.AuthResponse
import Gneiss.PacketCompiler.Service.Test
import Gneiss.PacketCompiler.Service.Users
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserManagementController {

    val userManagementDao = UserDao()
    val userService = Users(userManagementDao)

    val jwtHelper = JWTHelper()
    val login = Login(jwtHelper, userManagementDao)
    val auth = Auth(jwtHelper, userManagementDao)

    @PostMapping("/test")
    fun CreateTest(@RequestBody req: CreateTestRequest): CreateTestResponse {
        return Test.CreateTest(req)
    }

    @PostMapping("/create")
    fun CreateUser(@RequestBody req: CreateUserRequest): ResponseEntity<String> {
        return userService.createUser(req)
    }

    @PostMapping("/login")
    fun Login(@RequestBody req: LoginRequest): ResponseEntity<LoginResponse> {
        return login.login(req)
    }

    @PostMapping("/auth")
    fun AuthenticateJWT(@RequestBody req: AuthRequest): ResponseEntity<AuthResponse> {
        return auth.authenticateJWT(req)
    }
}
