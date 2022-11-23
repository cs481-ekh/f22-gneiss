package Gneiss.PacketCompiler.Service

import Gneiss.PacketCompiler.DatabaseAccess.IPacketDao
import Gneiss.PacketCompiler.Helpers.IJWTHelper
import Gneiss.PacketCompiler.Helpers.IPDFHelper
import Gneiss.PacketCompiler.Helpers.JWTBody
import Gneiss.PacketCompiler.Models.Packet
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer  
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
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

class PacketPostResponse()

class PacketPatchResponse()

class ApprovalPDFPostResponse()

class InvoicePDFPostResponse()

class PacketGetAllResponse(
    val allKeys: MutableSet<Packet>
)

class PacketGetSingleResponse(
    val packetBuffer: ByteBuffer
)

@Service
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

    fun getSinglePacket(jwt: String, id: String): ResponseEntity<PacketGetSingleResponse> {
        // Parse the jwt to get the users username and role, if null return a Unauthorized http response
        val jwtBody: JWTBody? = jwtHelper.parseJWT(jwt)

        if (jwtBody == null) {
            return ResponseEntity<PacketGetSingleResponse>(PacketGetSingleResponse(ByteBuffer.allocate(0)), HttpStatus.UNAUTHORIZED)
        }

        // Do something to get the file location via the packetDao (get user from jwt for key and id is field)
        // If the user is an admin there is additional work to do, as they may not be the user who owns the packet
        var packetToReturn: Packet
        if (jwtBody.role == "user") {
            packetToReturn = packetDao.get(jwtBody.user, id)
        } else {
            //TODO: WORK FOR ADMIN
            packetToReturn = Packet("name", "invoice", "approval", "csv", "compiled")
        }
        
        // Get the pdf file as a byte array
        val packetFile = File(packetToReturn.compiledPDFPath)
        val pdfByteBuffer = readFile(packetFile)

        // Define the headers needed to specify we are returning a pdf
        //TODO: LET THE USER CHANGE BETWEEN INLINE (browser view), AND ATTACHMENT (download)
        val contentDisposition: ContentDisposition = ContentDisposition.builder("attachment").filename("Filename").build()

        val headers: HttpHeaders = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_PDF)
        headers.setContentLength(pdfByteBuffer.limit())
        headers.setContentDisposition(contentDisposition)

        return ResponseEntity<PacketGetSingleResponse>(PacketGetSingleResponse(pdfByteBuffer), headers, HttpStatus.OK)
    }

    fun getAllPackets(jwt: String): ResponseEntity<PacketGetAllResponse> {
        // Get the user from the jwt using the parse method and dereferencing from the JWTBody
        val jwtBody: JWTBody? = jwtHelper.parseJWT(jwt)

        // If there is no auth header return an empty response
        if (jwtBody == null) {
            return ResponseEntity<PacketGetAllResponse>(PacketGetAllResponse(mutableSetOf<Packet>()), HttpStatus.UNAUTHORIZED)
        }

        // If the user has higher permissions than user return all the packets, else return only those made by that specific user
        var allKeys: MutableSet<Packet>
        if (jwtBody.role == "user") {
            allKeys = packetDao.getUserKeys(jwtBody.user)
        } else {
            allKeys = packetDao.getAllKeys()
        }

        return ResponseEntity<PacketGetAllResponse>(PacketGetAllResponse(allKeys), HttpStatus.OK)
    }

    fun readFile (File file): ByteBuffer {
        DataInputStream dataInputStream = null;
        try {
            var byteCount = file.length()

            var fileInputStream = FileInputStream(file)
            var dataInputStream = DataInputStream(fileInputStream)
            var bytes = ByteArray(byteCount)
            dataInputStream.readFully(bytes)

            return ByteBuffer.wrap(bytes)
        } 
        catch (e: IOException) {
            return ByteBuffer.allocate(0)
        }
        finally {
            dataInputStream.close()
        }
    }
}
