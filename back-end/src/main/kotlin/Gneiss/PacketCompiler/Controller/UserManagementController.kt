package Gneiss.PacketCompiler.Controller

import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.JWTHelper
import Gneiss.PacketCompiler.Service.Auth
import Gneiss.PacketCompiler.Service.AuthResponse
import Gneiss.PacketCompiler.Service.CreateTestRequest
import Gneiss.PacketCompiler.Service.CreateTestResponse
import Gneiss.PacketCompiler.Service.CreateUserRequest
import Gneiss.PacketCompiler.Service.DemoteUserRequest
import Gneiss.PacketCompiler.Service.GetUsersResponse
import Gneiss.PacketCompiler.Service.Login
import Gneiss.PacketCompiler.Service.LoginRequest
import Gneiss.PacketCompiler.Service.LoginResponse
import Gneiss.PacketCompiler.Service.PromoteUserRequest
import Gneiss.PacketCompiler.Service.SetBanUserRequest
import Gneiss.PacketCompiler.Service.Test
import Gneiss.PacketCompiler.Service.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserManagementController @Autowired constructor(var jwtHelper: JWTHelper) {

    val userManagementDao = UserDao()
    val userService = Users(jwtHelper, userManagementDao)

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
        return auth.authenticateJWT(headers.getOrDefault("authorization", "invalid"))
    }

    @PostMapping("/promote")
    fun PromoteUser(@RequestHeader headers: Map<String, String>, @RequestBody req: PromoteUserRequest): ResponseEntity<Void> {
        return userService.promoteUser(headers.getOrDefault("authorization", "invalid"), req)
    }

    @PostMapping("/demote")
    fun PromoteUser(@RequestHeader headers: Map<String, String>, @RequestBody req: DemoteUserRequest): ResponseEntity<Void> {
        return userService.demoteUser(headers.getOrDefault("authorization", "invalid"), req)
    }

    @PostMapping("/ban")
    fun PromoteUser(@RequestHeader headers: Map<String, String>, @RequestBody req: SetBanUserRequest): ResponseEntity<Void> {
        return userService.setBanUser(headers.getOrDefault("authorization", "invalid"), req)
    }

    @GetMapping("/")
    fun GetUsers(@RequestHeader headers: Map<String, String>): ResponseEntity<GetUsersResponse> {
        return userService.getUsers(headers.getOrDefault("authorization", "invalid"))
    }
}
