package Gneiss.PacketCompiler.Service

class PacketPostRequest(val invoicePDFPath: String, val approvalPDFPath: String, val csvPDFPath: String, val compiledPDFPath: String)

class PacketPostResponse()

class Packet {
    companion object {
        fun packetPost(req: PacketPostRequest): PacketPostResponse {
            return PacketPostResponse()
        }
    }
}
