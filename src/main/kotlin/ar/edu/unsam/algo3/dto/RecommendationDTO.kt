package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.model.Recomendacion
import ar.edu.unsam.algo3.model.Usuario

data class RecommendationDTO(
    val id: Int?,
    val name: String,
    val isPrivate: Boolean,
    val description: String,
    val recommendedBooks: List<String>,
    val averageRating: Double,
    val numberOfBooks: Int,
    val readingTime: Double,
    val canRate: Boolean,
    val isOwner: Boolean,
    var isFavorite: Boolean,
) {

    companion object {
        fun fromRecommendation(usuario: Usuario, recommendation: Recomendacion): RecommendationDTO {
            return RecommendationDTO(
                id = recommendation.id,
                name = nameTruncate(recommendation),
                isPrivate = recommendation.esPrivado,
                description = descriptionTruncate(recommendation),
                recommendedBooks = recommendation.librosRecomendados.take(4).map { it.getNombre() },
                averageRating = recommendation.promedioValoraciones(),
                numberOfBooks = recommendation.librosRecomendados.size,
                readingTime = recommendation.tiempoDeLecturaTotal(usuario),
                canRate = recommendation.puedeValorar(usuario),
                isOwner = recommendation.esCreador(usuario),
                isFavorite = usuario.recomendacionesPorValorar.contains(recommendation)
            )
        }

        private fun descriptionTruncate(recommendation: Recomendacion): String {
            return if (recommendation.descripcion.length < 110) {
                recommendation.descripcion
            } else recommendation.descripcion.substring(0, 110) + "..."
        }

        private fun nameTruncate(recommendation: Recomendacion): String {
            return if (recommendation.nombre.length < 36) {
                recommendation.nombre
            } else recommendation.nombre.substring(0, 36) + "..."
        }
    }
}

data class RecommendationDetailsDTO(
    val id: Int?,
    val name: String,
    val description: String,
    val recommendedBooks: List<BookDTO>,
    val averageRating: Double,
    val canRate: Boolean,
    val valoration: List<ValorationDTO>,
    val private: Boolean
) {

    companion object {
        fun fromRecommendation(usuario: Usuario, recommendation: Recomendacion): RecommendationDetailsDTO {
            return RecommendationDetailsDTO(
                id = recommendation.id,
                name = recommendation.nombre,
                description = recommendation.descripcion,
                recommendedBooks = recommendation.librosRecomendados.map { BookDTO.fromBookToDTO(it) },
                averageRating = recommendation.promedioValoraciones(),
                canRate = recommendation.puedeValorar(usuario),
                valoration = recommendation.valoraciones.values.toList().map { ValorationDTO.fromValoration(it) },
                private = recommendation.esPrivado
            )
        }
    }
}

data class SearchDTO(
    val searchValue: String,
    val privateCheck: Boolean
)