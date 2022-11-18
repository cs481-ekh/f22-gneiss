package Gneiss.PacketCompiler.Helpers

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component

@Component
class JWTHelper() : IJWTHelper {
    val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    override fun createJWT(body: JWTBody): String {
        val jwt = Jwts.builder()
            .setSubject(body.user)
            .claim("role", body.role)
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
