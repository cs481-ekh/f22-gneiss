package Gneiss.PacketCompiler.Helpers

interface IPDFHelper {
    fun getTextFromPDF(pdfBytes: ByteArray): String
    fun htmlToPDF(fileName: String, htmlText: String)
}
