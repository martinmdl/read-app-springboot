package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.model.Autor
import ar.edu.unsam.algo3.model.Lenguaje

data class AuthorDTO(
    var id: Int? = null,
    var fullName: String,
    val nativeLanguage: Lenguaje,
) {
    companion object {
        fun fromAuthorToDTO(autor: Autor): AuthorDTO {
            return AuthorDTO(
                id = autor.id,
                fullName = autor.getNombre() + " " + autor.getApellido(),
                nativeLanguage = autor.lenguaNativa
            )
        }
    }
}

data class AuthorDetailsDTO(
    var id: Int? = null,
    var name: String,
    var lastName: String,
    val nativeLanguage: Lenguaje,
) {
    companion object {
        fun fromAuthorToDTO(autor: Autor): AuthorDetailsDTO {
            return AuthorDetailsDTO(
                id = autor.id,
                name = autor.getNombre(),
                lastName = autor.getApellido(),
                nativeLanguage = autor.lenguaNativa
            )
        }
    }
}