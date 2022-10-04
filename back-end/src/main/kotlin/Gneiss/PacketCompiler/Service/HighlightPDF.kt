package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.Helpers.IPDFHelper

class HighlightPDFRequest(val outputName: String, val fileBytes: ByteArray, val highlightWords: Array<String>)
class HighlightPDFResponse(val fileName: String)

class HighlightPDF(pdfHelper: IPDFHelper) {

    var pdfHelper = pdfHelper

    fun highlightPDF(req: HighlightPDFRequest): HighlightPDFResponse {
        var pdfText = pdfHelper.getTextFromPDF(req.fileBytes)

        var htmlBuilder = StringBuilder()
        pdfText.split("\n").forEach {
            htmlBuilder.append(String.format("<p>%s</p>", it))
        }
        var result = htmlBuilder.toString()

        req.highlightWords.forEach {
            result = result.replace(it, String.format("<span style='background-color:yellow;'>%s</span>", it), true)
        }

        pdfHelper.htmlToPDF(req.outputName + ".pdf", result)

        return HighlightPDFResponse(req.outputName)
    }
}
