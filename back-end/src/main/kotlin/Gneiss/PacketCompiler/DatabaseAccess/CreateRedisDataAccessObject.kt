package Gneiss.PacketCompiler.DatabaseAccess

import io.github.crackthecodeabhi.kreds.connection.newClient
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking { // this: CoroutineScope
    launch {
        newClient(Endpoint.from("127.0.0.1:6379")).use { client ->
            client.set("foo","100") 
            println("incremented value of foo ${client.incr("foo")}") // prints 101
            client.expire("foo",3u) // set expiration to 3 seconds
            delay(3000)
            assert(client.get("foo") == null)
        }
    }
}