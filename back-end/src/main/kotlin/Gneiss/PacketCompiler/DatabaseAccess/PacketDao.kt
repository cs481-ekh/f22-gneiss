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

    override fun delete(key: String, field: String): Boolean {
        val jedis = pool.getResource()
        var ret: Long
        jedis.use {
            ret = jedis.hdel(key, field)
        }
        if (ret == 1L) {
            return true
        }
        return false
    }

    override fun getAllKeys(): Map<String, Packet> {
        val jedis = pool.getResource()
        val allKeys: Set<String> = jedis.keys("USER#*")

        val res = mutableMapOf<String, Packet>()
        jedis.use {
            for (key in allKeys) {
                val allPacketsForUser = jedis.hgetAll(key)
                allPacketsForUser.forEach { entry ->
                    res[entry.key] = jsonSerializer.deserializePacket(entry.value)
                }
            }
        }

        return res
    }

    override fun getUserKeys(user: String): Map<String, Packet> {
        val jedis = pool.getResource()
        val res = mutableMapOf<String, Packet>()
        jedis.use {
            // Get a set of all the fields (packets) for a corresponding key (user)
            // allPacketsHash will be a map<String, String> with the first string being the field and the second being a packet
            val stringPacketMap = jedis.hgetAll("USER#" + user)

            stringPacketMap.forEach { entry ->
                res[entry.key] = jsonSerializer.deserializePacket(entry.value)
            }
        }

        return res
    }
}
