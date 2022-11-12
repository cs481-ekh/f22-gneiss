package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.IPacketDao
import Gneiss.PacketCompiler.Helpers.IJWTHelper
import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Helpers.ICSVHelper
import Gneiss.PacketCompiler.Models.Packet
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

class PacketPostRequest(
    val name: String,
    val invoicePDFPath: String,
    val approvalPDFPath: String,
    val csvPDFPath: String,
    val compiledPDFPath: String
)

class PacketPatchRequest(
    val name: String?,
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

class CsvPDFPostRequest(
    val outputName: String,
    val fileBytes: ByteArray
)

class PacketPostResponse()

class PacketPatchResponse()

class ApprovalPDFPostResponse()

class InvoicePDFPostResponse()

class csvPDFPostResponse()

class PacketRequestHandler(pdfHelper: IPDFHelper, packetDao: IPacketDao) {

    var pdfHelper = pdfHelper
    var packetDao = packetDao
    var jwtHelper = jwtHelper

    fun packetPost(user: String, id: String, req: PacketPostRequest): PacketPostResponse {
        var packet = Packet(req.name, req.invoicePDFPath, req.approvalPDFPath, req.csvPDFPath, req.compiledPDFPath)
        packetDao.set(user, id, packet)
        return PacketPostResponse()
    }

    fun packetPatch(user: String, id: String, req: PacketPatchRequest): PacketPatchResponse {
        val packet = packetDao.get(user, id)
        if (req.name != null) {
            packet.name = req.name
        }
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

        packetPatch(user, id, PacketPatchRequest(null, null, req.outputName, null, null))
        return ApprovalPDFPostResponse()
    }

    fun invoicePDFPost(user: String, id: String, req: InvoicePDFPostRequest): InvoicePDFPostResponse {
        //When given a json, its in a package of a string, it writes to file
        pdfHelper.writeFile(req.outputName, req.fileBytes)
        packetPatch(user, id, PacketPatchRequest(null, req.outputName, null, null, null))
        return InvoicePDFPostResponse()
    }

    fun csvPDFPost(user: String, id: String, req: CsvPDFPostRequest): csvPDFPostResponse {
        pdfHelper.writeFile(req.outputName, req.fileBytes)
        var pdf = csvToPDF(csv)
        packetPatch(user, id, PacketPatchRequest(null, req.outputName, null, null, null))
        // Load source file CSV for conversion
           //var converter = Converter(csv);
        // Prepare conversion options for target format PDF
           //var convertOptions = FileType().fromExtension("pdf").getConvertOptions();
        // Convert to PDF format
           //converter.convert("output.pdf", convertOptions);
        
                   //receives a csv string, and it writes to a pdf file
        //id refers to packet, pdf will be put in packet
        //read pdf helper, see if it can help
        //convert csv to pdf using csvToPDF in pdf helper
        //after pdf is created send it off to packet id
        return csvPDFPostResponse()
    }
}
