package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.model.Identidad
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class LoginRequest(val username: String, val password: String)

data class AuthResponse(val token: String)

class AuthenticatedUser(
    override var id: Int?,
    val userName: String,
    private val password: String,
    private val rol: String
) : UserDetails, Identidad {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(this.rol))
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return userName
    }
}
