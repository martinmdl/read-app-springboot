package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dto.AuthenticatedUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    private val secretKey = "15641494648646846874494684864948949848949849848946496475646465494696468944"
    fun getToken(user: AuthenticatedUser): String {
        println(user.id)
        val claims: MutableMap<String, Any> = HashMap()
        claims["id"] = user.id ?: -1
        return getToken(claims, user)
    }

    fun getUsernameFromToken(token: String): String {
        return getClaim(token, Claims::getSubject)
    }

    fun getUserIdFromToken(token: String): Int {
        return getClaim(token) { claims -> claims["id"] as Int }
    }

    fun isTokenValid(token: String, userDetails: UserDetails?): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails?.username && !isTokenExpired(token)
    }

    fun <T> getClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    private fun getExpirationDateFromToken(token: String): Date {
        return getClaim(token, Claims::getExpiration)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun getToken(claims: MutableMap<String, Any>, user: AuthenticatedUser): String {
        return Jwts
            .builder()
            .claims(claims)
            .subject(user.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getKey())
            .compact()
    }

    private fun getKey(): SecretKey? {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    }
}