package Gneiss.PacketCompiler.Helpers

import Gneiss.PacketCompiler.Models.Packet
import com.google.gson.Gson

class JsonSerializer() : IJsonSerializer {
    override fun serializePacket(packet: Packet): String {
        val gson = Gson()
        return gson.toJson(packet)
    }

    override fun deserializePacket(json: String): Packet {
        val gson = Gson()
        return gson.fromJson(json, Packet::class.java)
    }
}
