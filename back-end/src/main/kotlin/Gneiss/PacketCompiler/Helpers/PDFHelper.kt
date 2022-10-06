package Gneiss.PacketCompiler.Helpers

import com.itextpdf.html2pdf.HtmlConverter
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler
import java.io.ByteArrayInputStream
import java.io.FileOutputStream

class PDFHelper() : IPDFHelper {
    override fun getTextFromPDF(pdfBytes: ByteArray): String {
        var handler = BodyContentHandler()
        var metadata = Metadata()
        var inputStream = ByteArrayInputStream(pdfBytes)
        var pcontext = ParseContext()

        var pdfparser = PDFParser(); pdfparser.parse(inputStream, handler, metadata, pcontext)
        return handler.toString()
    }
    override fun htmlToPDF(fileName: String, htmlText: String) {
        var fileOutputStream = FileOutputStream(fileName)
        HtmlConverter.convertToPdf(htmlText, fileOutputStream)
    }
}
