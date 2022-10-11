package Gneiss.PacketCompiler.Helpers

class JWTBody(val user: String, val role: Int)

interface IJWTHelper {
    fun createJWT(body: JWTBody): String
    fun parseJWT(jwtString: String): JWTBody?
}
