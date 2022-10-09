package Gneiss.PacketCompiler.Controller

import Gneiss.PacketCompiler.Helpers.PDFHelper
import Gneiss.PacketCompiler.Service.HighlightPDF
import Gneiss.PacketCompiler.Service.HighlightPDFRequest
import Gneiss.PacketCompiler.Service.HighlightPDFResponse
import Gneiss.PacketCompiler.Service.Packet
import Gneiss.PacketCompiler.Service.PacketPostRequest
import Gneiss.PacketCompiler.Service.PacketPostResponse
import Gneiss.PacketCompiler.Service.PacketPatchRequest
import Gneiss.PacketCompiler.Service.PacketPatchResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.multipart.MultipartFile
import java.util.Date

@RestController
class PacketController {

    var outputPrefix = "output/"
    var pdfHelper = PDFHelper()
    var highlightPDF = HighlightPDF(pdfHelper)

    @PostMapping("/api/highlightpdf")
    fun FileUpload(@RequestParam("file") file: MultipartFile, @RequestParam("highlightWords") highlightWords: Array<String>): HighlightPDFResponse {
        var outputName = Date().getTime().toString()
        return highlightPDF.highlightPDF(
            HighlightPDFRequest(
                outputName = outputPrefix + outputName,
                fileBytes = file.getBytes(),
                highlightWords = highlightWords
            )
        )
        // TODO: PATCH
    }

    @PostMapping("/api/packet")
    fun PacketPost(@RequestBody req: PacketPostRequest): PacketPostResponse {
        return Packet.packetPost(req)
    }

    @PatchMapping("/api/packet")
    fun PacketPatch(@RequestBody req: PacketPatchRequest): PacketPatchResponse {
        return Packet.packetPatch(req)
    }
}
