package Gneiss.PacketCompiler.DatabaseAccess

import Gneiss.PacketCompiler.Models.Packet

interface IPacketDao {
    fun set(key: String, field: String, packet: Packet)
    fun get(key: String, field: String): Packet
}