package Gneiss.PacketCompiler.Controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*
import Gneiss.PacketCompiler.Service.*

@RestController
@RequestMapping("account")
class AccountController {
    @PostMapping("/test")
    fun CreateTest(@RequestBody req: CreateTestRequest): CreateTestResponse {
        return Test.CreateTest(req)
    }

    @PostMapping("/create")
    fun CreateAccount(@RequestBody req: CreateAccountRequest): ResponseEntity<String> {
        return Users.createAccount(req)
    }

    @PostMapping("/login")
    fun Login(@RequestBody req: LoginRequest): LoginResponse{
        return Login.login(req)
    }
}
