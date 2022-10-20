package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Helpers.IJsonSerializer
import Gneiss.PacketCompiler.Service.ApprovalPDFPostRequest
import Gneiss.PacketCompiler.Service.InvoicePDFPostRequest
import Gneiss.PacketCompiler.Service.PacketRequestHandler
import Gneiss.PacketCompiler.Service.PacketPostRequest
import Gneiss.PacketCompiler.Service.PacketPatchRequest
import Gneiss.PacketCompiler.DatabaseAccess.IPacketDao
import Gneiss.PacketCompiler.Models.Packet
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PacketRequestHandlerTests {

    var pdfHelper = mockk<IPDFHelper>()
    var packetDao = mockk<IPacketDao>()

    var packetOutput = slot<Packet>()
    var htmlOutput = slot<String>()
    var byteOutput = slot<ByteArray>()

    fun getHandler(): PacketRequestHandler {
        return PacketRequestHandler(pdfHelper, packetDao)
    }

    @Test
    fun packetPostWorks() {
        every { packetDao.set(any(), any(), capture(packetOutput)) } just Runs
        var packetHandler = getHandler()

        packetHandler.packetPost("user", "id", PacketPostRequest("a", "b", "c", "d"))
        assertThat(packetOutput.equals(Packet("a","b","c","d")))
    }

    @Test
    fun packetPatchWorks() {
        every { packetDao.set(any(), any(), capture(packetOutput)) } just Runs
        every { packetDao.get(any(), any()) } returns Packet("a","b","c","d")
        var packetHandler = getHandler()

        packetHandler.packetPatch("user", "id", PacketPatchRequest("grapes", null, null, null))
        assertThat(packetOutput.equals(Packet("grapes","b","c","d")))
    }

    @Test
    fun approvalPDFPostTextIsHighlighted() {
        every { pdfHelper.getTextFromPDF(any()) } returns "Hello text"
        every { pdfHelper.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var packetHandler = getHandler() 

        val request = ApprovalPDFPostRequest("packetId", "outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost(request)

        assertThat(htmlOutput.captured).isEqualTo("<p>Hello <span style='background-color:yellow;'>text</span></p>")
    }

    @Test
    fun approvalPDFPostHighlightWordsIsCaseInsensitive() {
        every { pdfHelper.getTextFromPDF(any()) } returns "Hello tEXt"
        every { pdfHelper.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var packetHandler = getHandler() 

        val request = ApprovalPDFPostRequest("packetId", "outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost(request)

        assertThat(htmlOutput.captured).isEqualTo("<p>Hello <span style='background-color:yellow;'>text</span></p>")
    }

    @Test
    fun approvalPDFPostPartialWordHighlightWorks() {
        every { pdfHelper.getTextFromPDF(any()) } returns "texting texted textbook"
        every { pdfHelper.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var packetHandler = getHandler() 

        val request = ApprovalPDFPostRequest("packetId", "outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost(request)

        assertThat(htmlOutput.captured).isEqualTo("<p><span style='background-color:yellow;'>text</span>ing <span style='background-color:yellow;'>text</span>ed <span style='background-color:yellow;'>text</span>book</p>")
    }

    @Test
    fun invoicePDFPostWorks() {
        every { pdfHelper.writeFile(any(), capture(byteOutput)) } just Runs
        var packetHandler = getHandler() 
        val request = InvoicePDFPostRequest("packetId", "outputName", ByteArray(0))
        packetHandler.invoicePDFPost(request)

        assertThat(byteOutput.captured).isEqualTo(ByteArray(0))
    }
}
