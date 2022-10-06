package Gneiss.PacketCompiler.Controller

import Gneiss.PacketCompiler.Service.CreateTestRequest
import Gneiss.PacketCompiler.Service.CreateTestResponse
import Gneiss.PacketCompiler.Service.CreateUserRequest
import Gneiss.PacketCompiler.Service.CreateUserResponse
import Gneiss.PacketCompiler.Service.Login
import Gneiss.PacketCompiler.Service.LoginRequest
import Gneiss.PacketCompiler.Service.LoginResponse
import Gneiss.PacketCompiler.Service.Test
import Gneiss.PacketCompiler.Service.Users
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserManagementController {
    @PostMapping("/test")
    fun CreateTest(@RequestBody req: CreateTestRequest): CreateTestResponse {
        return Test.CreateTest(req)
    }

    @PostMapping("/user")
    fun CreateUser(@RequestBody req: CreateUserRequest): CreateUserResponse {
        return Users.createUser(req)
    }

    @PostMapping("/login")
    fun Login(@RequestBody req: LoginRequest): LoginResponse {
        return Login.login(req)
    }
}
