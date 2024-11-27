package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.model.Lenguaje
import ar.edu.unsam.algo3.model.Libro
import org.intellij.lang.annotations.Language


data class BookDTO(
    val id: Int?,
    val bookTitle: String,
    val authorName: String,
    val pagesCount: Int,
    val wordsCount: Int,
    val languages: Set<Lenguaje>,
    val weekSales: Int
) {
    companion object {
        fun fromBookToDTO(book: Libro): BookDTO {
            return BookDTO(
                id = book.id,
                bookTitle = nameTruncate(book.getNombre()),
                authorName = book.autor.getNombre(),
                pagesCount = book.paginas,
                wordsCount = book.cantPalabras,
                languages = book.idioma,
                weekSales = book.getVentasSemanales()
            )
        }

        private fun nameTruncate(name: String): String {
            return if (name.length < 25) {
                name
            } else name.substring(0, 25) + "..."
        }
    }
}

data class BookAdminDTO(
    val id: Int?,
    val authorID: Int?,
    val bookTitle: String,
    val authorName: String,
    val pagesCount: Int,
    val wordsCount: Int,
    val numberEditions: Int,
    val nativeLanguage: Lenguaje,
    val languages: Set<Lenguaje>,
    val weekSales: Int,
    val complexLecture: Boolean,
    val bestSeller: Boolean
) {
    companion object {
        fun fromBookToDTO(book: Libro): BookAdminDTO {
            return BookAdminDTO(
                id = book.id,
                authorID = book.autor.id,
                bookTitle = nameTruncate(book.getNombre()),
                authorName = book.autor.getNombre() + " " + book.autor.getApellido(),
                pagesCount = book.paginas,
                wordsCount = book.cantPalabras,
                numberEditions = book.getEdiciones(),
                nativeLanguage = book.lenguajeNativo,
                languages = book.idioma,
                weekSales = book.getVentasSemanales(),
                complexLecture = book.esDesafiante(),
                bestSeller = book.esBestSeller()
            )
        }

        private fun nameTruncate(name: String): String {
            return if (name.length < 25) {
                name
            } else name.substring(0, 25) + "..."
        }
    }
}