@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.RepositorioRecomendaciones
import ar.edu.unsam.algo3.dao.RepositorioUsuario
import ar.edu.unsam.algo3.dto.BookDTO
import ar.edu.unsam.algo3.dto.NewValorationDTO
import ar.edu.unsam.algo3.dto.RecommendationDetailsDTO
import ar.edu.unsam.algo3.dto.SearchDTO
import ar.edu.unsam.algo3.model.Libro
import ar.edu.unsam.algo3.model.Recomendacion
import ar.edu.unsam.algo3.model.Usuario
import org.springframework.stereotype.Service

@Service
class RecommendationService(
    val repositorioRecomendaciones: RepositorioRecomendaciones,
    val repositorioUsuario: RepositorioUsuario,
    val bookService: BookService
) {
    fun getAllRecommendationsAllowedById(currentUser: Usuario): List<Recomendacion> {
        val allRecommendations = repositorioRecomendaciones.getAll()
        val allowedRecommendations = recommendationAllowed(currentUser, allRecommendations)
        return allowedRecommendations
    }

    fun getOwnRecommendationsAllowedById(currentUser: Usuario): List<Recomendacion> {
        return currentUser.listaDeRecomendacionesCreada
    }

    fun recommendationToEvaluate(user: Usuario): List<Recomendacion> {
        return user.recomendacionesPorValorar.toList()
    }

    fun addRecommendationToEvaluate(user: Usuario, idRecommendation: Int) {
        val recommendationToAdd = repositorioRecomendaciones.getById(idRecommendation)
        user.agregarRecomendacionPorValorar(recommendationToAdd)
    }

    fun removeRecommendationToEvaluate(user: Usuario, idRecommendation: Int) {
        val recommendationToRemove = repositorioRecomendaciones.getById(idRecommendation)
        user.eliminarRecomendacionPorValorar(recommendationToRemove)
    }

    private fun recommendationAllowed(user: Usuario, recommendation: List<Recomendacion>): List<Recomendacion> {
        return recommendation.filter {
            it.puedeLeerLaRecomendacion(user) && (it.creador == user || user.perfilDeRecomendacion.validarRecomendacion(
                it
            ))
        }
    }

    fun favoriteRecommendationStatus(user: Usuario, id: Int): Boolean {
        val recommendation = getRecommendationById(id)
        return user.recomendacionesPorValorar.contains(recommendation)
    }

    fun deleteRecommendation(user: Usuario, id: Int) {
        val recommendation = getRecommendationById(id)
        user.listaDeRecomendacionesCreada.remove(recommendation)

        val userList = repositorioUsuario.getAll()
        userList.forEach {
            if (it.recomendacionesPorValorar.contains(recommendation)) it.recomendacionesPorValorar.remove(
                recommendation
            )
        }

        repositorioRecomendaciones.delete(recommendation)
    }

    fun filterRecommendations(searchDTO: SearchDTO, currentUser: Usuario): List<Recomendacion> {
        val allRecommendations = repositorioRecomendaciones.search(searchDTO.searchValue)
        val allowedRecommendations = recommendationAllowed(currentUser, allRecommendations)
        return if (searchDTO.privateCheck) {
            allowedRecommendations.filter { it.esPrivado }
        } else {
            allowedRecommendations
        }
    }

    fun getRecommendationById(id: Int) = repositorioRecomendaciones.getById(id)

    fun getBooksIds(idReco: Int): List<Int> {
        val recommendation = getRecommendationById(idReco)
        return recommendation.librosRecomendados.mapNotNull { it.id }
    }

    fun createValoration(user: Usuario, idRecomendacion: Int, valorationDTO: NewValorationDTO) {
        val recommendation = getRecommendationById(idRecomendacion)
        user.valorarRecomendacion(recommendation, valorationDTO.value, valorationDTO.comment)
    }

    fun deleteBookInRecommendation(idReco: Int, book: Libro) {
        getRecommendationById(idReco).eliminarLibro(book)
    }

    fun addBookRecommendation(idReco: Int, book: Libro, currentUser: Usuario) {
        getRecommendationById(idReco).agregarALibrosDeRecomendacion(currentUser, book)
    }

    fun updateRecommendation(
        currentUser: Usuario,
        recommendation: Recomendacion,
        recommendationDTO: RecommendationDetailsDTO
    ) {
        recommendation.nombre = recommendationDTO.name
        recommendation.descripcion = recommendationDTO.description
        val books = booksDTOtoBooks(recommendationDTO.recommendedBooks)
            .filter { listBooks ->
                recommendation.librosRecomendados.none { it.id == listBooks.id }
            }
        books.forEach { recommendation.agregarALibrosDeRecomendacion(currentUser, it) }
        recommendation.esPrivado = recommendationDTO.private
    }

    fun getRecommendationAmount(): Int = repositorioRecomendaciones.getAll().size

    private fun booksDTOtoBooks(listBooksDTO: List<BookDTO>): List<Libro> =
        listBooksDTO.map { bookService.getBookById(it.id!!) }
}