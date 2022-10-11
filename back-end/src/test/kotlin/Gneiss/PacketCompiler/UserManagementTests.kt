package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.DatabaseAccess.CredentialsResponse
import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.JWTHelper
import Gneiss.PacketCompiler.Service.CreateUserRequest
import Gneiss.PacketCompiler.Service.Login
import Gneiss.PacketCompiler.Service.LoginRequest
import Gneiss.PacketCompiler.Service.Users
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest
class UserManagementTests() {

    var databaseAccess = mockk<UserDao>()
    var jwtHelper = mockk<JWTHelper>()

    @Test
    fun validLoginCredentials() {
        every { databaseAccess.validateCredentials(any(), any()) } returns CredentialsResponse(true, (1).toString())
        every { jwtHelper.createJWT(any()) } returns "someJWT"

        val loginService = Login(jwtHelper, databaseAccess)

        val loginRequest = LoginRequest("someEmail@email.com", "somePassword")
        val responseExpected = ResponseEntity<String>("someJWT", HttpStatus.OK)
        val responseActual = loginService.login(loginRequest)

        assertThat(responseActual.getStatusCode()).isEqualTo(responseExpected.getStatusCode())
    }

    @Test
    fun invalidLoginCredentials() {
        every { databaseAccess.validateCredentials(any(), any()) } returns CredentialsResponse(false, (-1).toString())
       
        val loginService = Login(jwtHelper, databaseAccess)

        val loginRequest = LoginRequest("someEmail@email.com", "somePassword")
        val responseExpected = ResponseEntity<String>("Invalid Credentials", HttpStatus.UNAUTHORIZED)
        val responseActual = loginService.login(loginRequest)

        assertThat(responseActual.getStatusCode()).isEqualTo(responseExpected.getStatusCode())
    }

    @Test
    fun userAccountCreated() {
        every { databaseAccess.checkAccountExists(any()) } returns false
        every { databaseAccess.createAccount(any(), any(), any(), any(), any()) } returns Unit

        val userService = Users(databaseAccess)

        val userRequest = CreateUserRequest("someEmail@email.com", "somePassword", "firstName", "lastName", 1)
        val responseExpected = ResponseEntity<String>("User account created successfully", HttpStatus.OK)
        val responseActual = userService.createUser(userRequest)

        assertThat(responseActual.getStatusCode()).isEqualTo(responseExpected.getStatusCode())
    }

    @Test
    fun userAccountNotCreated() {
        every { databaseAccess.checkAccountExists(any()) } returns true

        val userService = Users(databaseAccess)

        val userRequest = CreateUserRequest("someEmail@email.com", "somePassword", "firstName", "lastName", 1)
        val responseExpected = ResponseEntity<String>("Account already exists for this email", HttpStatus.NOT_ACCEPTABLE)
        val responseActual = userService.createUser(userRequest)

        assertThat(responseActual.getStatusCode()).isEqualTo(responseExpected.getStatusCode())
    }
}
