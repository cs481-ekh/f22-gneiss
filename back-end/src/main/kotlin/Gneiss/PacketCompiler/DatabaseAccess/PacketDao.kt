package Gneiss.PacketCompiler.DatabaseAccess

import Gneiss.PacketCompiler.Helpers.IJsonSerializer
import Gneiss.PacketCompiler.Models.Packet
import redis.clients.jedis.JedisPool

class PacketDao(jsonSerializer: IJsonSerializer) : IPacketDao {

    var jsonSerializer = jsonSerializer
    var pool: JedisPool = JedisPool()

    init {
        var port: String? = System.getenv("REDIS_PORT")
        if (port != null) {
            pool = JedisPool(System.getenv("REDIS_HOST"), port.toInt())
        }
    }

    override fun set(key: String, field: String, packet: Packet) {
        val jedis = pool.getResource()
        jedis.use {
            jedis.hset(key, field, jsonSerializer.serializePacket(packet))
        }
    }

    override fun get(key: String, field: String): Packet {
        val jedis = pool.getResource()
        lateinit var ret: Packet
        jedis.use {
            ret = jsonSerializer.deserializePacket(jedis.hget(key, field))
        }
        return ret
    }
}