package Gneiss.PacketCompiler.Controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import Gneiss.PacketCompiler.Service.*

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
    fun Login(@RequestBody req: LoginRequest): LoginResponse{
        return Login.login(req)
    }
}
