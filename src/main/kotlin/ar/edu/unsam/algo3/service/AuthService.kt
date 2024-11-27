package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.UserAuthenticatedRepository
import ar.edu.unsam.algo3.dto.AuthResponse
import ar.edu.unsam.algo3.dto.AuthenticatedUser
import ar.edu.unsam.algo3.dto.LoginRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    val authenticationManager: AuthenticationManager,
    val userAuthenticatedRepository: UserAuthenticatedRepository,
    private val jwtService: JwtService
) {
    fun login(loginRequest: LoginRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )
        val user: AuthenticatedUser = userAuthenticatedRepository.findByUsername(loginRequest.username)
        val token: String = jwtService.getToken(user)
        return AuthResponse(token)
    }
}