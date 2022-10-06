package Gneiss.PacketCompiler.Helpers

import io.jsonwebtoken.Jwts

class JWTHelper() : IJWTHelper {
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    override fun createJWT(): String {
        val jws = Jwts.builder()
            .setSubject("Bob")
            .signWith("")
            .compact(); 
        return ""
    }
}