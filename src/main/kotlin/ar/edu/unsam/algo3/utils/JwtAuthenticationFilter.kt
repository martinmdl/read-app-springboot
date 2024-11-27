package ar.edu.unsam.algo3.utils

import ar.edu.unsam.algo3.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(val jwtService: JwtService, val userDetailsService: UserDetailsService) :
    OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getTokenFromRequest(request)
        if (token == null) {
            println("No JWT token found in request")
            filterChain.doFilter(request, response)
            return
        }

        val username = jwtService.getUsernameFromToken(token)
        println("Username from JWT: $username")

        if (SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)
            if (jwtService.isTokenValid(token, userDetails)) {
                println("Token is valid for user: $username")
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            } else {
                println("Invalid JWT token for user: $username")
            }
        }

        filterChain.doFilter(request, response)
    }


    fun getTokenFromRequest(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")
        return if (header != null && header.startsWith("Bearer ")) {
            header.substring(7)
        } else {
            null
        }
    }
}