package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.RepositorioAutores
import ar.edu.unsam.algo3.dao.RepositorioLibros
import ar.edu.unsam.algo3.dao.RepositorioRecomendaciones
import ar.edu.unsam.algo3.dto.BookAdminDTO
import ar.edu.unsam.algo3.dto.SearchDTO
import ar.edu.unsam.algo3.exceptions.Businessexception
import ar.edu.unsam.algo3.model.Libro
import ar.edu.unsam.algo3.model.Usuario
import org.springframework.stereotype.Service

@Service
class BookService(
    private val repositorioLibros: RepositorioLibros,
    private val repositorioAutores: RepositorioAutores,
    private val repositorioRecomendaciones: RepositorioRecomendaciones,
) {

    fun createBook(bookAdminDTO: BookAdminDTO) {
        val newBook = Libro(
            id= bookAdminDTO.id,
            nombre = bookAdminDTO.bookTitle,
            editorial = "", // PREGUNTAR
            paginas = bookAdminDTO.pagesCount,
            cantPalabras = bookAdminDTO.wordsCount,
            lecturaCompleja = bookAdminDTO.complexLecture,
            ediciones = bookAdminDTO.numberEditions,
            idioma = bookAdminDTO.languages + bookAdminDTO.nativeLanguage,
            ventasSemanales = bookAdminDTO.weekSales,
            autor = repositorioAutores.getById(bookAdminDTO.authorID!!),
            lenguajeNativo = bookAdminDTO.nativeLanguage
        )
        println(newBook)
        repositorioLibros.create(newBook)
    }


    fun updateBook(idBook: Int, book: BookAdminDTO) {
        val currentAuthor = repositorioAutores.getById(book.authorID!!)
        val currentBook = repositorioLibros.getById(idBook)
        currentBook.autor = currentAuthor
        currentBook.setNombre(book.bookTitle)
        currentBook.paginas = book.pagesCount
        currentBook.cantPalabras = book.wordsCount
        currentBook.setLecturaCompleja(book.complexLecture)
        currentBook.setEdiciones(book.numberEditions)
        currentBook.setVentaSemanales(book.weekSales)
        currentBook.idioma = (listOf(book.nativeLanguage) + book.languages.filter { it != book.nativeLanguage }).toSet()
        currentBook.lenguajeNativo = book.nativeLanguage
    }

    fun getBookById(idBook: Int): Libro = repositorioLibros.getById(idBook)

    fun getAllBooks(): List<Libro> = repositorioLibros.getAll()

    fun getReadBooks(currentUser: Usuario): List<Libro> = currentUser.librosLeidos.map { it.key }

    fun getBooksToRead(currentUser: Usuario): List<Libro> = currentUser.librosPorLeer.toList()

    fun deleteBookToRead(user: Usuario, idBook: Int) {
        val book = this.getBookById(idBook)
        user.eliminarLibroPorLeer(book)
    }

    fun deleteReadBooks(user: Usuario, idBook: Int) {
        val book = this.getBookById(idBook)
        user.deleteLibroLeido(book)
    }

    fun deleteBook(idBook: Int) {
        val book = repositorioLibros.getById(idBook)

        if (bookInRecommendation(book)) {
            throw Businessexception("El libro está relacionado con una recomendacion y no se puede eliminar.")
        }
        try {
            repositorioLibros.delete(book)
        } catch (e: Businessexception) {
            println("No se puede eliminar al libro: " + e.message)
        }
    }

    fun filterBooks(filter: SearchDTO, currentUser: Usuario): List<Libro> {
        val filteredBooks = repositorioLibros.search(filter.searchValue)
        return filteredBooks
    }

    fun getBooksToAddToReadingList(currentUser: Usuario): List<Libro> {
        return getAllBooks().filter {
            !getBooksToRead(currentUser).contains(it) && !getReadBooks(currentUser).contains(
                it
            )
        }
    }

    fun getBooksToAddToReadList(currentUser: Usuario): List<Libro> {
        return getAllBooks().filter { !getReadBooks(currentUser).contains(it) }
    }

    fun getBooksToAddInRecommendation(booksIds: List<Int>, currentUser: Usuario): List<Libro> {
        val readBooks = getReadBooks(currentUser)
        return readBooks.filter { !booksIds.contains(it.id) }
    }

    fun getBooksAmount(): Int = getAllBooks().size

    private fun bookInRecommendation(book: Libro): Boolean {
        return repositorioRecomendaciones.getAll().any { it -> it.librosRecomendados.any { it == book } }
    }
}