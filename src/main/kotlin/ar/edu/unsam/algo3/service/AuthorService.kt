package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.RepositorioAutores
import ar.edu.unsam.algo3.dao.RepositorioLibros
import ar.edu.unsam.algo3.dto.AuthorDetailsDTO
import ar.edu.unsam.algo3.dto.SearchDTO
import ar.edu.unsam.algo3.exceptions.Businessexception
import ar.edu.unsam.algo3.model.Autor
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AuthorService(
    private val repositoryAuthor: RepositorioAutores,
    private val repositoryBook: RepositorioLibros
) {

    fun getAllAuthors(): List<Autor> = repositoryAuthor.getAll()

    fun getAuthor(id: Int): Autor = repositoryAuthor.getById(id)

    fun deleteAuthor(id: Int) {
        val author = getAuthor(id)
        if (authorInBook(author)) {
            throw Businessexception("El autor está relacionado con libros y no se puede eliminar.")
        }
        try {
            repositoryAuthor.delete(author)
        } catch (e: Businessexception) {
            println("No se puede eliminar al autor: " + e.message)
        }
    }

    fun updateAuthor(author: Autor, authorDetailsDTO: AuthorDetailsDTO) {
        try {
            if (!validateAuthorDTO(authorDetailsDTO)) {
                author.setNombre(authorDetailsDTO.name)
                author.setApellido(authorDetailsDTO.lastName)
                author.lenguaNativa = authorDetailsDTO.nativeLanguage
            }
        } catch (e: Businessexception) {
            println("No se puede editar al autor" + e.message)
        }
    }

    fun createAuthor(authorDetailsDTO: AuthorDetailsDTO) {
        try {
            if (!validateAuthorDTO(authorDetailsDTO)) {
                val newAuthor = Autor(
                    id = null,
                    nombre = authorDetailsDTO.name,
                    apellido = authorDetailsDTO.lastName,
                    seudonimo = "",
                    fechaNac = LocalDate.now(),
                    lenguaNativa = authorDetailsDTO.nativeLanguage,
                    premios = 0
                )
                repositoryAuthor.create(newAuthor)
            }
        } catch (e: Businessexception) {
            println("No se puede crear autor" + e.message)
        }

    }

    fun filterAuthor(searchDTO: SearchDTO): List<Autor> = repositoryAuthor.search(searchDTO.searchValue)

    private fun validateAuthorDTO(authorDetailsDTO: AuthorDetailsDTO): Boolean {
        return specialChar(authorDetailsDTO.name + authorDetailsDTO.lastName) && number(authorDetailsDTO.name + authorDetailsDTO.lastName)
    }

    private fun specialChar(str: String): Boolean = str.matches(".*[^\\w\\s].*".toRegex())

    private fun number(str: String): Boolean = str.matches(".*\\d.*".toRegex())

    private fun authorInBook(author: Autor): Boolean = repositoryBook.getAll().any { it.autor == author }
}