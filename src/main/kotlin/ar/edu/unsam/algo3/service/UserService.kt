@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.RepositorioUsuario
import ar.edu.unsam.algo3.dto.UserDTO
import ar.edu.unsam.algo3.model.*
import org.springframework.stereotype.Service

@Service
class UserService(val repositorioUsuario: RepositorioUsuario, val jwtService: JwtService) {

    fun getUser(id: Int): Usuario = repositorioUsuario.getById(id)

    fun getUserFromBearerToken(bearerToken: String): Usuario {
        // Verifica que el token empiece con "Bearer "
        println("------------------------------------------------------")
        if (bearerToken.startsWith("Bearer ")) {
            // Extrae el token sin "Bearer "
            val token = bearerToken.substring(7)
            val userID = jwtService.getUserIdFromToken(token)
            return this.getUser(userID)
        }
        // Si el token no empieza con "Bearer
        throw IllegalArgumentException("Token inválido")
    }

    fun addBookToRead(book: Libro, user: Usuario) {
        user.agregarLibroPorLeer(book)
    }

    fun readBook(book: Libro, user: Usuario) {
        if (user.librosPorLeer.contains(book)) {
            user.eliminarLibroPorLeer(book)
        }
        user.leerLibro(book)
    }

    fun getAllFriends(user: Usuario) = user.amigos

    fun deleteFriend(idFriend: Int, currentUser: Usuario) {
        val friendToDelete = getUser(idFriend)
        friendToDelete.recomendacionesPorValorar.removeIf {
            it.esPrivado && it.creador == currentUser
        }
        currentUser.eliminarAmigo(friendToDelete)
    }

    fun addFriend(idFriend: Int, currentUser: Usuario) {
        val friendToAdd = getUser(idFriend)
        currentUser.agregarAmigo(friendToAdd)
    }

    fun convertStringTotypeOfReader(usuario: Usuario, tipoDeLector: String): TipoLector {
        return when (tipoDeLector.lowercase()) {
            "lector promedio" -> LectorPromedio(usuario)
            "lector normal" -> LectorNormal(usuario)
            "lector ansioso" -> LectorAnsioso(usuario)
            "lector fanatico" -> LectorFanatico(usuario)
            "lector recurrente" -> LectorRecurrente(usuario)
            else -> throw IllegalArgumentException("Tipo de lector no reconocido: $tipoDeLector")
        }
    }

    fun convertStringToSearchCriteria(usuario: Usuario, perfilStr: List<String>): PerfilDeRecomendacion {
        return if (perfilStr.size > 1) {
            Combinador(
                usuario,
                perfilesCombinados = perfilStr.map { selectPerfilDeRecomendacion(usuario, it) }.toMutableSet()
            )
        } else {
            selectPerfilDeRecomendacion(usuario, perfilStr[0])
        }
    }

    fun selectPerfilDeRecomendacion(usuario: Usuario, item: String): PerfilDeRecomendacion {
        return when (item.lowercase()) {
            "precavido" -> Precavido(usuario)
            "leedor" -> Leedor(usuario)
            "poliglota" -> Poliglota(usuario)
            "nativista" -> Nativista(usuario)
            "calculador" -> Calculador(usuario)
            "demandante" -> Demandante(usuario)
            "experimentado" -> Experimentado(usuario)
            "cambiante" -> Cambiante(usuario)
            else -> throw IllegalArgumentException("Perfil de recomendación desconocido: $item")
        }
    }

    fun updateProfile(usuario: Usuario, userDto: UserDTO) {
        usuario.nombre = userDto.name
        usuario.apellido = userDto.lastName
        usuario.username = userDto.username
        usuario.fechaNac = userDto.dateOfBirth
        usuario.direccionEmail = userDto.mail
        usuario.palabrasPorMinuto = userDto.wordsPerMinutes
        usuario.tipoLector = this.convertStringTotypeOfReader(usuario, userDto.typeOfReader)
        usuario.perfilDeRecomendacion = this.convertStringToSearchCriteria(usuario, userDto.searchCriteria)
    }

    fun getAllUsers(currentUser: Usuario): List<Usuario> {
        val userList = repositorioUsuario.getAll()
        return userList.filter {
            it.id != currentUser.id && !currentUser.amigos.contains(it)
        }
    }

    fun getAllInactiveUsers(): Int = repositorioUsuario.getAll().filter{ !it.esActivo() }.size

    fun getUserAmount(): Int = repositorioUsuario.getAll().size

    fun deleteInactiveUsers() { repositorioUsuario.removeInactiveUsers() }
}