package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Service.Users
import Gneiss.PacketCompiler.Service.CreateUserRequest
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity

@SpringBootTest
class UserManagementTests() { 

    var databaseAccess = mockk<UserDao>()

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
        every {databaseAccess.checkAccountExists(any())} returns true

        val userService = Users(databaseAccess)

        val userRequest = CreateUserRequest("someEmail@email.com", "somePassword", "firstName", "lastName", 1)
        val responseExpected = ResponseEntity<String>("Account already exists for this email", HttpStatus.NOT_ACCEPTABLE)
        val responseActual = userService.createUser(userRequest)

        assertThat(responseActual.getStatusCode()).isEqualTo(responseExpected.getStatusCode())
    }
}
