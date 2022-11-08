package Gneiss.PacketCompiler.Helpers

import com.itextpdf.html2pdf.HtmlConverter
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream

class PDFHelper() : IPDFHelper {
    override fun writeFile(fileName: String, bytes: ByteArray) {
        var file = File(fileName)
        file.writeBytes(bytes)
    }
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
    override fun csvToPDF(csvText: String) {
        //Split comma seperate line
        //Map the whole line that's been split into a string array
        //Read file content/string array into byte array
        //Convert array of string to a list of string with array.tostring
        //Convert array.tostring into a pdf table
        //Return PDF
        var textCSV = csvText
    }
}
