package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Service.ApprovalPDFPostRequest
import Gneiss.PacketCompiler.Service.InvoicePDFPostRequest
import Gneiss.PacketCompiler.Service.PacketRequestHandler
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

    var htmlOutput = slot<String>()
    var byteOutput = slot<ByteArray>()
    var service = mockk<IPDFHelper>()

    @Test
    fun approvalPDFPostTextIsHighlighted() {
        every { service.getTextFromPDF(any()) } returns "Hello text"
        every { service.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var packetHandler = PacketRequestHandler(service)

        val request = ApprovalPDFPostRequest("packetName", "outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost(request)

        assertThat(htmlOutput.captured).isEqualTo("<p>Hello <span style='background-color:yellow;'>text</span></p>")
    }

    @Test
    fun approvalPDFPostHighlightWordsIsCaseInsensitive() {
        every { service.getTextFromPDF(any()) } returns "Hello tEXt"
        every { service.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var packetHandler = PacketRequestHandler(service)

        val request = ApprovalPDFPostRequest("packetName", "outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost(request)

        assertThat(htmlOutput.captured).isEqualTo("<p>Hello <span style='background-color:yellow;'>text</span></p>")
    }

    @Test
    fun approvalPDFPostPartialWordHighlightWorks() {
        every { service.getTextFromPDF(any()) } returns "texting texted textbook"
        every { service.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var packetHandler = PacketRequestHandler(service)

        val request = ApprovalPDFPostRequest("packetName", "outputName", ByteArray(0), arrayOf("text"))
        packetHandler.approvalPDFPost(request)

        assertThat(htmlOutput.captured).isEqualTo("<p><span style='background-color:yellow;'>text</span>ing <span style='background-color:yellow;'>text</span>ed <span style='background-color:yellow;'>text</span>book</p>")
    }

    @Test
    fun invoicePDFPostWorks() {
        every { service.writeFile(any(), capture(byteOutput)) } just Runs
        var packetHandler = PacketRequestHandler(service)
        val request = InvoicePDFPostRequest("packetName", "outputName", ByteArray(0))
        packetHandler.invoicePDFPost(request)

        assertThat(byteOutput.captured).isEqualTo(ByteArray(0))
    }
}
