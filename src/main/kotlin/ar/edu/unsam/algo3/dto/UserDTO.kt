package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.model.*
import java.time.LocalDate

data class UserDTO(
    val name: String,
    val lastName: String,
    val username: String,
    val dateOfBirth: LocalDate,
    val mail: String,
    val wordsPerMinutes: Int,
    val typeOfReader: String,
    val searchCriteria: List<String>,
    val minRange: Int,
    val maxRange: Int
) {
    companion object {
        fun fromUserToDTO(user: Usuario): UserDTO {
            return UserDTO(
                name = user.nombre,
                lastName = user.apellido,
                username = user.username,
                dateOfBirth = user.fechaNac,
                mail = user.direccionEmail,
                wordsPerMinutes = user.palabrasPorMinuto,
                typeOfReader = user.tipoLector.nameClass,
                searchCriteria = generateSearchCriteria(user.perfilDeRecomendacion),
                minRange = asigneRange(user.perfilDeRecomendacion, true),
                maxRange = asigneRange(user.perfilDeRecomendacion, false)
            )
        }

        private fun generateSearchCriteria(perfil: PerfilDeRecomendacion): List<String> {
            return if (perfil is Combinador) {
                perfil.perfilesCombinados.map { it.nameClass }
            } else {
                listOf(perfil.nameClass)
            }
        }

        private fun asigneRange(perfil: PerfilDeRecomendacion, isMinRange: Boolean): Int {
            return when (perfil) {
                is Cambiante -> {
                    if (isMinRange) perfil.rangoMin else perfil.rangoMax
                }

                is Calculador -> {
                    if (isMinRange) perfil.rangoMin else perfil.rangoMax
                }

                is Combinador -> {
                    val results = perfil.perfilesCombinados.mapNotNull {
                        val rango = asigneRange(it, isMinRange)
                        if (rango > 0 || (isMinRange && it is Cambiante && it.rangoMin == null)) rango else null
                    }
                    if (isMinRange) results.minOrNull() ?: 0 else results.maxOrNull() ?: 0
                }

                else -> 0
            }
        }
    }
}


data class FriendDTO(
    // val ruta_imagen = string ??
    val name: String,
    val username: String,
    val id: Int?
) {
    companion object {
        fun fromFriendsToDTO(friend: Usuario): FriendDTO {
            return FriendDTO(
                name = friend.nombre + " " + friend.apellido,
                username = friend.username,
                id = friend.id
            )
        }
    }
}