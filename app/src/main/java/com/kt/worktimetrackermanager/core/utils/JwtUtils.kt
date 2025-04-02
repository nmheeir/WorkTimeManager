package com.kt.worktimetrackermanager.core.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


object JwtUtils {

    private val secretKey: SecretKey

    init {
        val secretString =
            "when you have eliminated the impossible, whatever remains, however improbable, must be the truth"
        val encodedString =
            Base64.getEncoder().encodeToString(secretString.toByteArray(StandardCharsets.UTF_8))

        // Giải mã chuỗi base64 thành mảng byte
        val keyBytes = Base64.getDecoder().decode(encodedString)
        this.secretKey = SecretKeySpec(keyBytes, "HmacSHA512")
    }

    private fun <T> extractClaims(token: String, claimsFunction: (Claims) -> T): T {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        return claimsFunction(claims)
    }

    fun extractUsername(token: String): String {
        return extractClaims(token) { claims ->
            claims["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name", String::class.java]
        }
    }

    fun extractRole(token: String): String {
        return extractClaims(token) { claims ->
            claims["http://schemas.microsoft.com/ws/2008/06/identity/claims/role", String::class.java]
        }
    }

    fun extractTeamId(token: String): Int {
        return extractClaims(token) { claims ->
            claims["http://schemas.microsoft.com/ws/2008/06/identity/claims/groupsid", String::class.java].toInt()
        }
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            val claims = extractClaims(token) { it }
            claims.expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            true
        }
    }
}
