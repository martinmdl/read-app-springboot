package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.LoginRequest
import ar.edu.unsam.algo3.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = ["http://localhost:5173"])
class LoginController(
    val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        return try {
            val authResponse = authService.login(loginRequest)
            ResponseEntity.ok(authResponse)
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "An unexpected error occurred"))
        }
    }
}