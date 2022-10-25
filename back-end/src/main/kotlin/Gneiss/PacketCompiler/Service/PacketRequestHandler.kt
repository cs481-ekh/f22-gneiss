package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.IPacketDao
import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Models.Packet

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
    val outputName: String,
    val fileBytes: ByteArray,
    val highlightWords: Array<String>
)

class InvoicePDFPostRequest(
    val outputName: String,
    val fileBytes: ByteArray
)

class PacketPostResponse()

class PacketPatchResponse()

class ApprovalPDFPostResponse()

class InvoicePDFPostResponse()

class PacketRequestHandler(pdfHelper: IPDFHelper, packetDao: IPacketDao) {

    var pdfHelper = pdfHelper
    var packetDao = packetDao

    fun packetPost(user: String, id: String, req: PacketPostRequest): PacketPostResponse {
        var packet = Packet(req.invoicePDFPath, req.approvalPDFPath, req.csvPDFPath, req.compiledPDFPath)
        packetDao.set(user, id, packet)
        return PacketPostResponse()
    }

    fun packetPatch(user: String, id: String, req: PacketPatchRequest): PacketPatchResponse {
        val packet = packetDao.get(user, id)
        if (req.invoicePDFPath != null) {
            packet.invoicePDFPath = req.invoicePDFPath
        }
        if (req.approvalPDFPath != null) {
            packet.approvalPDFPath = req.approvalPDFPath
        }
        if (req.csvPDFPath != null) {
            packet.csvPDFPath = req.csvPDFPath
        }
        if (req.compiledPDFPath != null) {
            packet.compiledPDFPath = req.compiledPDFPath
        }
        packetDao.set(user, id, packet)
        return PacketPatchResponse()
    }

    fun approvalPDFPost(user: String, id: String, req: ApprovalPDFPostRequest): ApprovalPDFPostResponse {
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

        packetPatch(user, id, PacketPatchRequest(null, req.outputName, null, null))
        return ApprovalPDFPostResponse()
    }

    fun invoicePDFPost(user: String, id: String, req: InvoicePDFPostRequest): InvoicePDFPostResponse {
        pdfHelper.writeFile(req.outputName, req.fileBytes)
        packetPatch(user, id, PacketPatchRequest(req.outputName, null, null, null))
        return InvoicePDFPostResponse()
    }
}
