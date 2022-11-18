package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.DatabaseAccess.CredentialsResponse
import Gneiss.PacketCompiler.DatabaseAccess.UserDao
import Gneiss.PacketCompiler.Helpers.IJWTHelper
import Gneiss.PacketCompiler.Helpers.JWTBody
import Gneiss.PacketCompiler.Service.CreateUserRequest
import Gneiss.PacketCompiler.Service.Login
import Gneiss.PacketCompiler.Service.LoginRequest
import Gneiss.PacketCompiler.Service.Users
import Gneiss.PacketCompiler.Service.PromoteUserRequest
import Gneiss.PacketCompiler.Service.DemoteUserRequest
import Gneiss.PacketCompiler.Service.SetBanUserRequest
import Gneiss.PacketCompiler.Models.User
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
    var jwtHelper = mockk<IJWTHelper>()

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
        every { databaseAccess.createAccount(any(), any(), any(), any()) } returns Unit

        val userService = Users(jwtHelper, databaseAccess)

        val userRequest = CreateUserRequest("someEmail@email.com", "somePassword", "firstName", "lastName")
        val responseExpected = ResponseEntity<String>("User account created successfully", HttpStatus.OK)
        val responseActual = userService.createUser(userRequest)

        assertThat(responseActual.getStatusCode()).isEqualTo(responseExpected.getStatusCode())
    }

    @Test
    fun userAccountNotCreated() {
        every { databaseAccess.checkAccountExists(any()) } returns true

        val userService = Users(jwtHelper, databaseAccess)

        val userRequest = CreateUserRequest("someEmail@email.com", "somePassword", "firstName", "lastName")
        val responseExpected = ResponseEntity<String>("Account already exists for this email", HttpStatus.NOT_ACCEPTABLE)
        val responseActual = userService.createUser(userRequest)

        assertThat(responseActual.getStatusCode()).isEqualTo(responseExpected.getStatusCode())
    }

    @Test
    fun getUsersReturnsListOfUsers() {
        val user = User("CharlieKelly@gmail.com", "user", true)
        every { databaseAccess.getUsers() } returns mutableListOf<User>(user)
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "admin")

        val userService = Users(jwtHelper, databaseAccess)
        val responseExpected = mutableListOf<User>(user)
        val responseActual = userService.getUsers("jwt")
        assertThat(responseActual.body!!.users).isEqualTo(responseExpected)
    }

    @Test
    fun nonAdminsCannotgetUsers() {
        val user = User("CharlieKelly@gmail.com", "user", true)
        every { databaseAccess.getUsers() } returns mutableListOf<User>(user)
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "user")

        val userService = Users(jwtHelper, databaseAccess)
        val responseActual = userService.getUsers("jwt")
        assertThat(responseActual.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun promoteUserSucceedsIfDaoOperationSucceeds() {
        every { databaseAccess.promoteUser(any()) } returns true
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "admin")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.promoteUser("jwt", PromoteUserRequest("DennisReynolds@gmail.com"))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun promoteUserFailsIfDaoOperationFails() {
        every { databaseAccess.promoteUser(any()) } returns false
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "admin")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.promoteUser("jwt", PromoteUserRequest("DeeReynolds@gmail.com"))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
    }
    
    @Test
    fun nonAdminCannotpromoteUser() {
        every { databaseAccess.promoteUser(any()) } returns false
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "user")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.promoteUser("jwt", PromoteUserRequest("DeeReynolds@gmail.com"))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun demoteUserSucceedsIfDaoOperationSucceeds() {
        every { databaseAccess.demoteUser(any()) } returns true
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "admin")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.demoteUser("jwt", DemoteUserRequest("FrankReynolds@gmail.com"))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun demoteUserFailsIfDaoOperationFails() {
        every { databaseAccess.demoteUser(any()) } returns false
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "admin")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.demoteUser("jwt", DemoteUserRequest("RonaldMacdonald@gmail.com"))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun nonAdminCannotdemoteUser() {
        every { databaseAccess.demoteUser(any()) } returns false
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "user")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.demoteUser("jwt", DemoteUserRequest("RonaldMacdonald@gmail.com"))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
    }


    @Test
    fun setBanUserSucceedsIfDaoOperationSucceeds() {
        every { databaseAccess.setBanUser(any(), any()) } returns true
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "admin")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.setBanUser("jwt", SetBanUserRequest("BooRadley@gmail.com", true))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun setBanUserFailsIfDaoOperationFails() {
        every { databaseAccess.setBanUser(any(), any()) } returns false
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "admin")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.setBanUser("jwt", SetBanUserRequest("ScoutFinch@gmail.com", true))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun nonAdminCannotsetBanUser() {
        every { databaseAccess.setBanUser(any(), any()) } returns false
        every { jwtHelper.parseJWT(any()) } returns JWTBody("user", "user")

        val userService = Users(jwtHelper, databaseAccess)
        val response = userService.setBanUser("jwt", SetBanUserRequest("ScoutFinch@gmail.com", true))
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}
