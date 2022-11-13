package Gneiss.PacketCompiler.DatabaseAccess

import Gneiss.PacketCompiler.Helpers.IJsonSerializer
import Gneiss.PacketCompiler.Models.Packet
import org.springframework.stereotype.Component
import redis.clients.jedis.JedisPool

@Component
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

    override fun getAllKeys(): Set<Packet> {
        val jedis = pool.getResource()
        val allKeys: Set<String> = jedis.keys("USER#*")

        var allPackets = mutableSetOf<Packet>()
        for (key in allKeys) {
            val allPacketsForUser = jedis.hgetAll(key)

            for (hash in allPacketsForUser) {
                allPackets.add(jsonSerializer.deserializePacket(hash))
            }
        }

        return allPackets
    }

    override fun getUserKeys(user: String): Set<Packet> {
        val jedis = pool.getResource()

        // Get a set of all the fields (packets) for a corresponding key (user)
        val allPacketsHash = jedis.hgetAll("USER#" + user)
        
        // Deserialize all the hashes back into packets
        val allPacketsForUser = mutableSetOf<Packet>()
        for (hash in allPacketsHash) {
            allPacketsForUser.add(jsonSerializer.deserializePacket(hash))
        }

        return allPacketsForUser
    }
}
