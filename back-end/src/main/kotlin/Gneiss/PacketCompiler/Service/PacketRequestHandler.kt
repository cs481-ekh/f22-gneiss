package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.IPacketDao
import Gneiss.PacketCompiler.Helpers.IJWTHelper
import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Helpers.JWTBody
import Gneiss.PacketCompiler.Models.Packet
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

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

class PacketPostResponse()

class PacketPatchResponse()

class ApprovalPDFPostResponse()

class InvoicePDFPostResponse()

class PacketGetResponse(
    val numKeys: Int,
    val allKeys: Set<String>
)

class PacketRequestHandler(pdfHelper: IPDFHelper, packetDao: IPacketDao, jwtHelper: IJWTHelper) {

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
        pdfHelper.writeFile(req.outputName, req.fileBytes)
        packetPatch(user, id, PacketPatchRequest(null, req.outputName, null, null, null))
        return InvoicePDFPostResponse()
    }

    fun getAllPackets(jwt: String): ResponseEntity<PacketGetResponse> {
        //Get the user from the jwt using the parse method and dereferencing from the JWTBody
        val jwtBody: JWTBody? = jwtHelper.parseJWT(jwt)
        println(jwtBody)
        println(jwtBody?.user + " " + jwtBody?.role)

        //If there is no auth header return an empty response
        if (jwtBody == null) {
            return ResponseEntity<PacketGetResponse>(PacketGetResponse(0, emptySet()), HttpStatus.UNAUTHORIZED)
        }
        
        //If the user has higher permissions than user return all the packets, else return only those made by that specific user
        var allKeys: Set<String>
        if (jwtBody.role == "user") {
            allKeys = packetDao.getUserKeys(jwtBody.user)
        } else {
            allKeys = packetDao.getAllKeys()
        }
        val numKeys = allKeys.size

        return ResponseEntity<PacketGetResponse>(PacketGetResponse(numKeys, allKeys), HttpStatus.OK)
    }
}
