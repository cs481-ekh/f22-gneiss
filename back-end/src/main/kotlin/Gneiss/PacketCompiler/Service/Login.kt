package Gneiss.PacketCompiler.Service
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
//get username and password, if valid they will return email and userRole and get passed to login class
class LoginRequest()

class LoginResponse()



class Login {
    companion object {
        fun login(req: LoginRequest):  LoginResponse{
            
            return LoginResponse()
        }
        fun createJWT(email:String, userrole:String):String {
            Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
            String jws = Jwts.builder().setSubject("Joe").signWith(key).compact()
            val signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)//string

            // Generate JWT String
            val exampleJwt = Jwts.builder().setSubject("Joe").signWith(signingKey).compact()//assign key, header, body then compact
            System.out.println("exampleJwt: $exampleJwt")
            // => exampleJwt: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKb2UifQ.EzFGmEj9KG9zrNsMBc0Y2YoUVuQJHL45uLrWFfj5CTBasArXAI-IEf_A0jYTKBZNhwLz3-NWEekPf8tll4yJVQ

            // Verify JWT String
            val claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(exampleJwt).body
            System.out.println("Decode result: $claims")
            // => Decode result: {sub=Joe}
            //https://blog.morizyun.com/java/library-jjwt-jwt-json-web-token.html
            // We need a signing key, so we'll create one just for this example. Usually
            // the key would be read from your application configuration instead.
            return jws
        }
    }
}







