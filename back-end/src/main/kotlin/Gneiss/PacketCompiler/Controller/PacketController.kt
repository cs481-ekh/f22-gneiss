package Gneiss.PacketCompiler.Controller

import Gneiss.PacketCompiler.DatabaseAccess.PacketDao
import Gneiss.PacketCompiler.Helpers.JWTHelper
import Gneiss.PacketCompiler.Helpers.JsonSerializer
import Gneiss.PacketCompiler.Helpers.PDFHelper
import Gneiss.PacketCompiler.Service.ApprovalPDFPostRequest
import Gneiss.PacketCompiler.Service.ApprovalPDFPostResponse
import Gneiss.PacketCompiler.Service.InvoicePDFPostRequest
import Gneiss.PacketCompiler.Service.InvoicePDFPostResponse
import Gneiss.PacketCompiler.Service.PacketGetAllResponse
import Gneiss.PacketCompiler.Service.PacketPatchRequest
import Gneiss.PacketCompiler.Service.PacketPatchResponse
import Gneiss.PacketCompiler.Service.PacketPostRequest
import Gneiss.PacketCompiler.Service.PacketPostResponse
import Gneiss.PacketCompiler.Service.PacketRequestHandler
import Gneiss.PacketCompiler.Service.SinglePacketRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.Date

@RestController
@RequestMapping("/api/packet")
class PacketController @Autowired constructor(var jwtHelper: JWTHelper) {

    var outputPrefix = "output/"
    var pdfHelper = PDFHelper()
    var jsonSerializer = JsonSerializer()
    var packetDao = PacketDao(jsonSerializer)
    var packetHandler = PacketRequestHandler(pdfHelper, packetDao, jwtHelper)

    @PostMapping("/approvalpdf/{id}")
    fun approvalPDF(@PathVariable id: String, @RequestParam("file") file: MultipartFile, @RequestParam("highlightWords") highlightWords: Array<String>): ApprovalPDFPostResponse {
        var outputName = Date().getTime().toString()
        return packetHandler.approvalPDFPost("USER#" + "user", id, ApprovalPDFPostRequest(outputPrefix + outputName + ".pdf", file.getBytes(), highlightWords))
    }

    @PostMapping("/invoicepdf/{id}")
    fun invoicePDF(@PathVariable id: String, @RequestParam("file") file: MultipartFile): InvoicePDFPostResponse {
        var outputName = Date().getTime().toString()
        return packetHandler.invoicePDFPost("USER#" + "user", id, InvoicePDFPostRequest(outputPrefix + outputName + ".pdf", file.getBytes()))
    }

    @PostMapping("/{id}")
    fun PacketPost(@PathVariable id: String, @RequestBody req: PacketPostRequest): PacketPostResponse {
        return packetHandler.packetPost("USER#" + "user", id, req)
    }

    @PatchMapping("/{id}")
    fun PacketPatch(@PathVariable id: String, @RequestBody req: PacketPatchRequest): PacketPatchResponse {
        return packetHandler.packetPatch("USER#" + "user", id, req)
    }

    // ID for a packet should be its name, as this is what will be downloaded/shown as the pdf files name to the user.
    @GetMapping("/retrieve/{id}")
    fun getSinglePacket(@PathVariable id: String, @RequestHeader headers: Map<String, String>, @RequestBody req: SinglePacketRequest): ResponseEntity<ByteArray> {
        // Get the jwt included in the headers - should be the Authorization header
        val jwt: String = headers.getOrDefault("authorization", "")

        return packetHandler.getSinglePacket(jwt, id, req)
    }

    @GetMapping("/retrieve")
    fun getAllPackets(@RequestHeader headers: Map<String, String>): ResponseEntity<PacketGetAllResponse> {
        // Get the jwt included in the headers - should be the Authorization header
        val jwt: String = headers.getOrDefault("authorization", "")

        return packetHandler.getAllPackets(jwt)
    }
}
