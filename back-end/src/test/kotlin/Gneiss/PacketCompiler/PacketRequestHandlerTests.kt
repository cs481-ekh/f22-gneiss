package Gneiss.PacketCompiler

import com.ninjasquad.springmockk.MockkBean
import Gneiss.PacketCompiler.DatabaseAccess.IPacketDao
import Gneiss.PacketCompiler.Helpers.IJWTHelper
import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Helpers.JWTBody
import Gneiss.PacketCompiler.Helpers.JWTHelper
import Gneiss.PacketCompiler.Models.Packet
import Gneiss.PacketCompiler.Service.ApprovalPDFPostRequest
import Gneiss.PacketCompiler.Service.InvoicePDFPostRequest
import Gneiss.PacketCompiler.Service.PacketGetResponse
import Gneiss.PacketCompiler.Service.PacketPatchRequest
import Gneiss.PacketCompiler.Service.PacketPostRequest
import Gneiss.PacketCompiler.Service.PacketRequestHandler
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest
class PacketRequestHandlerTests {

    var pdfHelper = mockk<IPDFHelper>()
    var packetDao = mockk<IPacketDao>()
    var jwtHelper: JWTHelper = mockk<JWTHelper>()

    var packetOutput = slot<Packet>()
    var htmlOutput = slot<String>()
    var byteOutput = slot<ByteArray>()

    fun getHandler(): PacketRequestHandler {
        return PacketRequestHandler(pdfHelper, packetDao, jwtHelper)
    }

    @Test
    fun packetPostWorks() {
        every { packetDao.set(any(), any(), capture(packetOutput)) } just Runs
        var packetHandler = getHandler()

        packetHandler.packetPost("user", "id", PacketPostRequest("name", "a", "b", "c", "d"))
        assertThat(packetOutput.equals(Packet("name", "a", "b", "c", "d")))
    }

    @Test
    fun packetPatchWorks() {
        every { packetDao.set(any(), any(), capture(packetOutput)) } just Runs
        every { packetDao.get(any(), any()) } returns Packet("name", "a", "b", "c", "d")
        var packetHandler = getHandler()

        packetHandler.packetPatch("user", "id", PacketPatchRequest(null, "grapes", null, null, null))
        assertThat(packetOutput.equals(Packet("name", "grapes", "b", "c", "d")))
    }

    @Test
    fun approvalPDFPostTextIsHighlighted() {
        every { pdfHelper.getTextFromPDF(any()) } returns "Hello text"
        every { pdfHelper.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        every { packetDao.set(any(), any(), capture(packetOutput)) } just Runs
        every { packetDao.get(any(), any()) } returns Packet("name", "a", "b", "c", "d")
        var packetHandler = getHandler()
        val request = ApprovalPDFPostRequest("outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost("user", "packetid", request)

        assertThat(htmlOutput.captured).isEqualTo("<p>Hello <span style='background-color:yellow;'>text</span></p>")
    }

    @Test
    fun approvalPDFPostHighlightWordsIsCaseInsensitive() {
        every { pdfHelper.getTextFromPDF(any()) } returns "Hello tEXt"
        every { pdfHelper.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        every { packetDao.set(any(), any(), capture(packetOutput)) } just Runs
        every { packetDao.get(any(), any()) } returns Packet("name", "a", "b", "c", "d")
        var packetHandler = getHandler()
        val request = ApprovalPDFPostRequest("outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost("user", "packetid", request)

        assertThat(htmlOutput.captured).isEqualTo("<p>Hello <span style='background-color:yellow;'>text</span></p>")
    }

    @Test
    fun approvalPDFPostPartialWordHighlightWorks() {
        every { pdfHelper.getTextFromPDF(any()) } returns "texting texted textbook"
        every { pdfHelper.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        every { packetDao.set(any(), any(), capture(packetOutput)) } just Runs
        every { packetDao.get(any(), any()) } returns Packet("name", "a", "b", "c", "d")
        var packetHandler = getHandler()
        val request = ApprovalPDFPostRequest("outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost("user", "packetid", request)

        assertThat(htmlOutput.captured).isEqualTo("<p><span style='background-color:yellow;'>text</span>ing <span style='background-color:yellow;'>text</span>ed <span style='background-color:yellow;'>text</span>book</p>")
    }

    @Test
    fun invoicePDFPostWorks() {
        every { pdfHelper.writeFile(any(), capture(byteOutput)) } just Runs
        every { packetDao.set(any(), any(), capture(packetOutput)) } just Runs
        every { packetDao.get(any(), any()) } returns Packet("name", "a", "b", "c", "d")
        var packetHandler = getHandler()
        val request = InvoicePDFPostRequest("outputName", ByteArray(0))
        packetHandler.invoicePDFPost("user", "packetid", request)

        assertThat(byteOutput.captured).isEqualTo(ByteArray(0))
    }

    @Test
    fun getAllPacketsTest() {
        every { packetDao.getAllKeys() } returns mutableSetOf<String>("somePacketId1", "somePacketId2")
        every { jwtHelper.parseJWT(any()) } returns JWTBody("username", "admin")

        var packetHandler = getHandler()
        val getAllPacketsResponse = packetHandler.getAllPackets("someJWT")
        val expectedResponse = ResponseEntity<PacketGetResponse>(PacketGetResponse(2, mutableSetOf<String>("somePacketId1", "somePacketId2")), HttpStatus.OK)
        assertThat(getAllPacketsResponse.equals(expectedResponse))
    }

    @Test
    fun getAllPacketsForUserTest() {
        every { packetDao.getUserKeys(any()) } returns mutableSetOf<String>("somePacketId1", "somePacketId2")
        every { jwtHelper.parseJWT(any()) } returns JWTBody("username", "user")

        var packetHandler = getHandler()
        val getUserPacketsResponse = packetHandler.getAllPackets("someJWT")
        val expectedResponse = ResponseEntity<PacketGetResponse>(PacketGetResponse(2, mutableSetOf<String>("somePacketId1", "somePacketId2")), HttpStatus.OK)
        assertThat(getUserPacketsResponse.equals(expectedResponse))
    }

    @Test
    fun getAllPacketsNoAuthTest() {
        every { jwtHelper.parseJWT(any()) } returns null

        var packetHandler = getHandler()
        val getAllPacketsAuthFailResponse = packetHandler.getAllPackets("someJWT")
        val expectedResponse = ResponseEntity<PacketGetResponse>(PacketGetResponse(0, emptySet()), HttpStatus.UNAUTHORIZED)
        assertThat(getAllPacketsAuthFailResponse.equals(expectedResponse))
    }
}
