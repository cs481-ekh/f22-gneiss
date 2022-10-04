package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Service.HighlightPDF
import Gneiss.PacketCompiler.Service.HighlightPDFRequest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HighlightPDFTests {

    var htmlOutput = slot<String>()
    var service = mockk<IPDFHelper>()

    @Test
    fun textIsHighlighted() {
        every { service.getTextFromPDF(any()) } returns "Hello text"
        every { service.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var pdfHighlighter = HighlightPDF(service)

        val request = HighlightPDFRequest("outputName", ByteArray(0), arrayOf("text"))
        pdfHighlighter.highlightPDF(request)

        assertThat(htmlOutput.captured).isEqualTo("<p>Hello <span style='background-color:yellow;'>text</span></p>")
    }

    @Test
    fun highlightWordsIsCaseInsensitive() {
        every { service.getTextFromPDF(any()) } returns "Hello tEXt"
        every { service.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var pdfHighlighter = HighlightPDF(service)

        val request = HighlightPDFRequest("outputName", ByteArray(0), arrayOf("text"))
        pdfHighlighter.highlightPDF(request)

        assertThat(htmlOutput.captured).isEqualTo("<p>Hello <span style='background-color:yellow;'>text</span></p>")
    }

    @Test
    fun partialWordHighlightWorks() {
        every { service.getTextFromPDF(any()) } returns "texting texted textbook"
        every { service.htmlToPDF(any(), capture(htmlOutput)) } just Runs
        var pdfHighlighter = HighlightPDF(service)

        val request = HighlightPDFRequest("outputName", ByteArray(0), arrayOf("text"))
        pdfHighlighter.highlightPDF(request)

        assertThat(htmlOutput.captured).isEqualTo("<p><span style='background-color:yellow;'>text</span>ing <span style='background-color:yellow;'>text</span>ed <span style='background-color:yellow;'>text</span>book</p>")
    }
}
