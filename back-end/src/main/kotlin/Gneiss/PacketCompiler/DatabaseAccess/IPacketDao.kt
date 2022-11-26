package Gneiss.PacketCompiler.DatabaseAccess

import Gneiss.PacketCompiler.Models.Packet

interface IPacketDao {
    fun set(key: String, field: String, packet: Packet)
    fun get(key: String, field: String): Packet
    fun delete(key: String, field: String): Boolean

    fun getAllKeys(): MutableSet<Packet>
    fun getUserKeys(user: String): MutableSet<Packet>

    fun mapToPacket(map: Map<String, String>): MutableSet<Packet>
}
