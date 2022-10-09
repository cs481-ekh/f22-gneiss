package Gneiss.PacketCompiler.Helpers

class JWTBody(val user: String, val role: String)

interface IJWTHelper {
    fun createJWT(body: JWTBody): String
    fun parseJWT(jwtString: String): JWTBody?
}
