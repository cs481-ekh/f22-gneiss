package Gneiss.PacketCompiler.Controller

import Gneiss.PacketCompiler.DatabaseAccess.PacketDao
import Gneiss.PacketCompiler.Helpers.JsonSerializer
import Gneiss.PacketCompiler.Helpers.PDFHelper
import Gneiss.PacketCompiler.Service.ApprovalPDFPostRequest
import Gneiss.PacketCompiler.Service.ApprovalPDFPostResponse
import Gneiss.PacketCompiler.Service.InvoicePDFPostRequest
import Gneiss.PacketCompiler.Service.InvoicePDFPostResponse
import Gneiss.PacketCompiler.Service.csvPDFPostResponse
import Gneiss.PacketCompiler.Service.PacketPatchRequest
import Gneiss.PacketCompiler.Service.PacketPatchResponse
import Gneiss.PacketCompiler.Service.PacketPostRequest
import Gneiss.PacketCompiler.Service.PacketPostResponse
import Gneiss.PacketCompiler.Service.PacketRequestHandler
import Gneiss.PacketCompiler.Service.CsvPDFPostRequest
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.Date

@RestController
@RequestMapping("/api/packet")
class PacketController {

    var outputPrefix = "output/"
    var pdfHelper = PDFHelper()
    var jsonSerializer = JsonSerializer()
    var packetDao = PacketDao(jsonSerializer)
    var packetHandler = PacketRequestHandler(pdfHelper, packetDao)

    @PostMapping("/approvalpdf/{id}")
    fun approvalPDF(@PathVariable id: String, @RequestParam("file") file: MultipartFile, @RequestParam("highlightWords") highlightWords: Array<String>): ApprovalPDFPostResponse {
        var outputName = Date().getTime().toString()
        return packetHandler.approvalPDFPost("user", id, ApprovalPDFPostRequest(outputPrefix + outputName + ".pdf", file.getBytes(), highlightWords))
    }

    @PostMapping("/invoicepdf/{id}")
    fun invoicePDF(@PathVariable id: String, @RequestParam("file") file: MultipartFile): InvoicePDFPostResponse {
        var outputName = Date().getTime().toString()
        return packetHandler.invoicePDFPost("user", id, InvoicePDFPostRequest(outputPrefix + outputName + ".pdf", file.getBytes()))
    }

    @PostMapping("/csvpdf/{id}")
    fun csvPDF(@PathVariable id: String, @RequestParam("file") file: MultipartFile): csvPDFPostResponse {
        var outputName = Date().getTime().toString()
        return packetHandler.csvPDFPost("user", id, CsvPDFPostRequest(outputPrefix + outputName + ".pdf", file.getBytes()))
    }

    //http://localhost:8080/api/packet/csvpdf/{id}
    //{id} is a place holder

    @PostMapping("/{id}")
    fun PacketPost(@PathVariable id: String, @RequestBody req: PacketPostRequest): PacketPostResponse {
        return packetHandler.packetPost("user", id, req)
    }

    @PatchMapping("/{id}")
    fun PacketPatch(@PathVariable id: String, @RequestBody req: PacketPatchRequest): PacketPatchResponse {
        return packetHandler.packetPatch("user", id, req)
    }
}
