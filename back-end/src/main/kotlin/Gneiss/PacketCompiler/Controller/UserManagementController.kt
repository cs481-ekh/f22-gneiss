package Gneiss.PacketCompiler.Controller

import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.JWTHelper
import Gneiss.PacketCompiler.Service.Auth
import Gneiss.PacketCompiler.Service.AuthResponse
import Gneiss.PacketCompiler.Service.CreateTestRequest
import Gneiss.PacketCompiler.Service.CreateTestResponse
import Gneiss.PacketCompiler.Service.CreateUserRequest
import Gneiss.PacketCompiler.Service.Login
import Gneiss.PacketCompiler.Service.LoginRequest
import Gneiss.PacketCompiler.Service.LoginResponse
import Gneiss.PacketCompiler.Service.Test
import Gneiss.PacketCompiler.Service.Users
import Gneiss.PacketCompiler.Service.GetUsersResponse
import Gneiss.PacketCompiler.Service.PromoteUserRequest
import Gneiss.PacketCompiler.Service.DemoteUserRequest
import Gneiss.PacketCompiler.Service.SetBanUserRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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
    fun AuthenticateJWT(@RequestHeader headers: Map<String, String>): ResponseEntity<AuthResponse> {
        return auth.authenticateJWT(headers.getOrDefault("authorization", ""))
    }

    @PostMapping("/promote")
    fun PromoteUser(@RequestBody req: PromoteUserRequest): ResponseEntity<Void> {
        return userService.promoteUser(req)
    }

    @PostMapping("/demote")
    fun PromoteUser(@RequestBody req: DemoteUserRequest): ResponseEntity<Void> {
        return userService.demoteUser(req)
    }

    @PostMapping("/ban")
    fun PromoteUser(@RequestBody req: SetBanUserRequest): ResponseEntity<Void> {
        return userService.setBanUser(req)
    }

    @GetMapping("/")
    fun GetUsers(): GetUsersResponse {
        return userService.getUsers()
    }
}
