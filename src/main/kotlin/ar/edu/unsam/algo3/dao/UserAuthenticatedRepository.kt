package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.dto.AuthenticatedUser
import org.springframework.security.core.userdetails.UsernameNotFoundException

@org.springframework.stereotype.Repository
class UserAuthenticatedRepository : Repository<AuthenticatedUser>() {
    fun findByUsername(username: String): AuthenticatedUser {
        return this.getAll().find { it.userName == username }
            ?: throw UsernameNotFoundException("Usuario con nombre $username no encontrado")
    }

    override fun update(obj: AuthenticatedUser) {
        val existingUserIndex = this.getById(obj.id!!).let { dataMap.values.indexOf(it) }
        if (existingUserIndex != -1) {
            dataMap[obj.id!!] = obj
        } else {
            throw IllegalArgumentException("User with id ${obj.id} not found")
        }
    }

    override fun search(regex: String): List<AuthenticatedUser> {
        val pattern = regex.toRegex()
        return dataMap.values.filter { pattern.containsMatchIn(it.userName) }
    }
}