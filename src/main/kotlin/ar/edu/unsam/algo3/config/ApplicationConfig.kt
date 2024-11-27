package ar.edu.unsam.algo3.config

import ar.edu.unsam.algo3.dao.UserAuthenticatedRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfig(private val repositoryUserDTO: UserAuthenticatedRepository) {

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailService())
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        return authenticationProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailService(): UserDetailsService {
        return UserDetailsService { username: String ->
            val user = repositoryUserDTO.findByUsername(username)
            user
        }
    }
}