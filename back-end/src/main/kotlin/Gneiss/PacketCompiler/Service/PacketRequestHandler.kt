package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.IPacketDao
import Gneiss.PacketCompiler.Helpers.IJWTHelper
import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Helpers.JWTBody
import Gneiss.PacketCompiler.Models.Packet
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files

class PacketDeleteRequest(
    val invoicePDFPath: String,
    val approvalPDFPath: String,
    val csvPDFPath: String,
    val compiledPDFPath: String
)

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

class SinglePacketRequest(
    val inlineFlag: Boolean,
    val compiledPDFPath: String
)

class PacketDeleteResponse()

class PacketPostResponse()

class PacketPatchResponse()

class ApprovalPDFPostResponse()

class InvoicePDFPostResponse()

class PacketGetAllResponse(
    val allKeys: Map<String, Packet>
)

@Service
class PacketRequestHandler(pdfHelper: IPDFHelper, packetDao: IPacketDao, jwtHelper: IJWTHelper) {

    var pdfHelper = pdfHelper
    var packetDao = packetDao
    var jwtHelper = jwtHelper

    fun packetDelete(user: String, id: String, req: PacketPostRequest): PacketDeleteResponse {
        packetDao.delete(user, id)
        return PacketDeleteResponse()
    }

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
        pdfHelper.writeFile(req.outputName, req.fileBytes)
        packetPatch(user, id, PacketPatchRequest(null, req.outputName, null, null, null))
        return InvoicePDFPostResponse()
    }

    fun getAllPackets(jwt: String): ResponseEntity<PacketGetAllResponse> {
        // Get the user from the jwt using the parse method and dereferencing from the JWTBody
        val jwtBody: JWTBody? = jwtHelper.parseJWT(jwt)

        // If there is no auth header return an empty response
        if (jwtBody == null) {
            return ResponseEntity<PacketGetAllResponse>(PacketGetAllResponse(mutableMapOf<String, Packet>()), HttpStatus.UNAUTHORIZED)
        }

        // If the user has higher permissions than user return all the packets, else return only those made by that specific user
        var allKeys: Map<String, Packet>
        if (jwtBody.role == "user") {
            allKeys = packetDao.getUserKeys(jwtBody.user)
        } else {
            allKeys = packetDao.getAllKeys()
        }

        return ResponseEntity<PacketGetAllResponse>(PacketGetAllResponse(allKeys), HttpStatus.OK)
    }

    fun getSinglePacket(jwt: String, id: String, req: SinglePacketRequest): ResponseEntity<ByteArray> {
        // Parse the jwt to get the users username and role, if null return a Unauthorized http response
        val jwtBody: JWTBody? = jwtHelper.parseJWT(jwt)

        if (jwtBody == null) {
            return ResponseEntity<ByteArray>(ByteArray(0), HttpStatus.UNAUTHORIZED)
        }

        // Get the pdf file as a byteBuffer
        val packetFile = File(req.compiledPDFPath)
        val packetSize = packetFile.length()
        val pdfByteArray: ByteArray = Files.readAllBytes(packetFile.toPath())

        // Define the headers needed to specify we are returning a pdf
        val contentDisposition: ContentDisposition
        if (req.inlineFlag) {
            contentDisposition = ContentDisposition.builder("inline").filename(id).build()
        } else {
            contentDisposition = ContentDisposition.builder("attachment").filename(id).build()
        }

        val headers: HttpHeaders = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_PDF)
        headers.setContentLength(packetSize)
        headers.setContentDisposition(contentDisposition)

        return ResponseEntity<ByteArray>(pdfByteArray, headers, HttpStatus.OK)
    }
}
