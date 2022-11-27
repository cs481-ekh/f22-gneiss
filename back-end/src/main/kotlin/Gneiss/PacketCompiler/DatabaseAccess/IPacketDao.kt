package Gneiss.PacketCompiler.DatabaseAccess

import Gneiss.PacketCompiler.Models.Packet

interface IPacketDao {
    fun set(key: String, field: String, packet: Packet)
    fun get(key: String, field: String): Packet
    fun delete(key: String, field: String): Boolean

    fun getAllKeys(): Map<String, Packet>
    fun getUserKeys(user: String): Map<String, Packet>
}
