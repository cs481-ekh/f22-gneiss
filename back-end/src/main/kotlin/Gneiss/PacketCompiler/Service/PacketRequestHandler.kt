package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.Helpers.IPDFHelper

class PacketPostRequest(
    val invoicePDFPath: String,
    val approvalPDFPath: String,
    val csvPDFPath: String,
    val compiledPDFPath: String
)

class PacketPatchRequest(
    val invoicePDFPath: String?,
    val approvalPDFPath: String?,
    val csvPDFPath: String?,
    val compiledPDFPath: String?
)

class ApprovalPDFPostRequest(
    val packetName: String,
    val outputName: String,
    val fileBytes: ByteArray,
    val highlightWords: Array<String>
)

class InvoicePDFPostRequest(
    val packetName: String,
    val outputName: String,
    val fileBytes: ByteArray
)

class PacketPostResponse()

class PacketPatchResponse()

class ApprovalPDFPostResponse()

class InvoicePDFPostResponse()

class PacketRequestHandler(pdfHelper: IPDFHelper) {

    var pdfHelper = pdfHelper

    fun packetPost(req: PacketPostRequest): PacketPostResponse {
        return PacketPostResponse()
    }

    fun packetPatch(req: PacketPatchRequest): PacketPatchResponse {
        // if (req.invoicePDFPath != null) {
        //     setInvoicePDFPath = req.invoicePDFPath
        // }
        // if (req.approvalPDFPath != null) {
        //     setApprovalPDFPath = req.approvalPDFPath
        // }
        // if (req.csvPDFPath != null) {
        //     setCsvPDFPath = req.csvPDFPath
        // }
        // if (req.compiledPDFPath != null) {
        //     setCompiledPDFPath = req.compiledPDFPath
        // }
        return PacketPatchResponse()
    }

    fun approvalPDFPost(req: ApprovalPDFPostRequest): ApprovalPDFPostResponse {
        var pdfText = pdfHelper.getTextFromPDF(req.fileBytes)

        var htmlBuilder = StringBuilder()
        pdfText.split("\n").forEach { htmlBuilder.append(String.format("<p>%s</p>", it)) }
        var result = htmlBuilder.toString()

        req.highlightWords.forEach {
            result =
                result.replace(
                    it,
                    String.format("<span style='background-color:yellow;'>%s</span>", it),
                    true
                )
        }

        pdfHelper.htmlToPDF(req.outputName, result)

        return ApprovalPDFPostResponse()
        // TODO PATCH
    }

    fun invoicePDFPost(req: InvoicePDFPostRequest): InvoicePDFPostResponse {
        pdfHelper.writeFile(req.outputName, req.fileBytes)
        return InvoicePDFPostResponse()
        // TODO PATCH
    }
}
