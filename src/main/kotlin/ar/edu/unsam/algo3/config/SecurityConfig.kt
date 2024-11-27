package ar.edu.unsam.algo3.config

import ar.edu.unsam.algo3.utils.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun SecurityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        authProvider: AuthenticationProvider
    ): SecurityFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authRequest
                ->
                authRequest
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().authenticated()
            }
            .sessionManagement { sessionManager ->
                sessionManager
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}