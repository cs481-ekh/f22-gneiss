package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.Service.Users
import Gneiss.PacketCompiler.Service.CreateUserRequest
import Gneiss.PacketCompiler.Service.CreateUserResponse
import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserManagementTests() {

    var databaseAccess = mockk<UserDao>()

    @Test
    fun userAccountCreated() {
        every {databaseAccess.checkAccountExists(any())} returns false
        every {databaseAccess.createAccount(any(), any(), any(), any(), any())} returns Unit

        val userService = Users(databaseAccess)

        val userRequest = CreateUserRequest("someEmail@email.com", "somePassword", "firstName", "lastName", 1)
        val responseExpected = CreateUserResponse("User account created successfully")
        val responseActual = userService.createUser(userRequest)

        assertThat(responseActual.responseString).isEqualTo(responseExpected.responseString)
    }

    @Test
    fun userAccountNotCreated() {
        every {databaseAccess.checkAccountExists(any())} returns true

        val userService = Users(databaseAccess)

        val userRequest = CreateUserRequest("someEmail@email.com", "somePassword", "firstName", "lastName", 1)
        val responseExpected = CreateUserResponse("Account already exists for this email")
        val responseActual = userService.createUser(userRequest)

        assertThat(responseActual.responseString).isEqualTo(responseExpected.responseString)
    }
}