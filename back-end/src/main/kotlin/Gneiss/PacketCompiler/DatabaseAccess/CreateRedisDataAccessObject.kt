package Gneiss.PacketCompiler.DatabaseAccess

import java.sql.Connection
import redis.clients.jedis.Jedis;
import org.springframework.stereotype.Service
import redis.clients.jedis.JedisPool

@Service
object CacheManager {
    private lateinit var jedisPool: JedisPool

    var connection: Connection? = null

    fun getConnection() {
        JedisPool pool = new JedisPool("localhost", 6379)
    }
}