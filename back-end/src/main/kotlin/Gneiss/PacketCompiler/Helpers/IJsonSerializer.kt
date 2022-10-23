package Gneiss.PacketCompiler.Helpers

import Gneiss.PacketCompiler.Models.Packet

interface IJsonSerializer {
    fun serializePacket(packet: Packet): String
    fun deserializePacket(json: String): Packet
}
