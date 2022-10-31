package Gneiss.PacketCompiler.DatabaseAccess

import Gneiss.PacketCompiler.Helpers.IJsonSerializer
import Gneiss.PacketCompiler.Models.Packet
import redis.clients.jedis.JedisPool
//import redis.clients.jedis.ScanParams
//import redis.clients.jedis.ScanResult

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

    override fun getAllKeys(): Set<String> {
        val jedis = pool.getResource()
        return jedis.keys("*")
        
        
        // //Setting up needed variables for scanning
        // var scanParams: ScanParams = ScanParams()
        // var cursor: String = "0"
        // var i = 0

        // //match function used for matching the key to some string (regex like)
        // //count used to set the amount of keys returned/scanned each iteration
        // scanParams.match("*")
        // //scanParams.count()

        // //
        // while (!"0".equals(cursor))


        // //temp return for verification
        // return setOf("something1", "something2")
    }
}
