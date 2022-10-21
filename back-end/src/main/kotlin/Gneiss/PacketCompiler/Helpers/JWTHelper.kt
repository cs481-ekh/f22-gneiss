package Gneiss.PacketCompiler.Helpers

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Date

class JWTHelper() : IJWTHelper {
    val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    override fun createJWT(body: JWTBody): String {
        //Create an expiration date 1 day in the future
        var exprDate: Date = Date()
        var dateAsLong = exprDate.getTime()
        dateAsLong += 86400000
        exprDate.setTime(dateAsLong)
        
        val jwt = Jwts.builder()
            .setSubject(body.user)
            .claim("role", body.role)
            .setExpiration(exprDate)
            .signWith(key)
            .compact()
        return jwt
    }

    override fun parseJWT(jwtString: String): JWTBody? {
        try {
            val jwt = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtString)
                .getBody()

            return JWTBody(user = jwt.get("sub") as String, jwt.get("role") as String)
        } catch (ex: JwtException) {
            return null
        }
    }
}
