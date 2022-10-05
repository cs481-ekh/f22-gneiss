package Gneiss.PacketCompiler.Service

class PacketPostRequest(val invoice: String, val approval: String, val csv: String, val compiled: String)

class PacketPostResponse()

class Packet {
    companion object {
        fun packetPost(req: PacketPostRequest): PacketPostResponse {
            return PacketPostResponse()
        }
    }
}
