package Gneiss.PacketCompiler.Service

class PacketPostRequest(val invoicePDFPath: String, val approvalPDFPath: String, val csvPDFPath: String, val compiledPDFPath: String)
class PacketPatchRequest(val invoicePDFPath: String?, val approvalPDFPath: String?, val csvPDFPath: String?, val compiledPDFPath: String?)

class PacketPostResponse()
class PacketPatchResponse()

class Packet {
    companion object {

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
    }
}
