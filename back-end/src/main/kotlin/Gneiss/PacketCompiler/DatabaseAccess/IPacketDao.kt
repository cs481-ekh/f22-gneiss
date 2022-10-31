package Gneiss.PacketCompiler.DatabaseAccess

import Gneiss.PacketCompiler.Models.Packet

interface IPacketDao {
    fun set(key: String, field: String, packet: Packet)
    fun get(key: String, field: String): Packet
    
    //Possible to add a parameter to let people get all keys that contain a certain string
    fun getAllKeys(): Set<String>
}
