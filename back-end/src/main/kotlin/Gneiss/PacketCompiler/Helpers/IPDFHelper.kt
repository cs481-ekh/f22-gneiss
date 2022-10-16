package Gneiss.PacketCompiler.Helpers

interface IPDFHelper {
    fun writeFile(fileName: String, bytes: ByteArray)
    fun getTextFromPDF(pdfBytes: ByteArray): String
    fun htmlToPDF(fileName: String, htmlText: String)
}
