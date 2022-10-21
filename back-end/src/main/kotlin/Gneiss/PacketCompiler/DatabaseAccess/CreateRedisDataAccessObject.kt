package Gneiss.PacketCompiler.DatabaseAccess

import java.sql.Connection
import java.sql.SQLException
import redis.clients.jedis.Jedis;
import org.springframework.stereotype.Service
import redis.clients.jedis.JedisPool
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
class redisDao {

        private lateinit var jedisPool: JedisPool

        
        var connection: Connection? = null

        
        fun getConnection() {
            try {
                
                jedisPool = JedisPool("jedisCache", 6379)
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        fun <T> get(key: String, clazz: Class<T>): T {
            val value: String = getJedis().get(key)
            val mapper = ObjectMapper().registerModule(KotlinModule())
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    
            // Temp addition, rectify later.
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    
            return mapper.readValue(value, clazz)
        }
    
        fun set(key: String, value: Any): Unit {
            val mapper = ObjectMapper().writeValueAsString(value)
            getJedis().set(key, mapper)
        }
        fun redisPost() {

        }

        fun redisPatch() {

        }
            

}