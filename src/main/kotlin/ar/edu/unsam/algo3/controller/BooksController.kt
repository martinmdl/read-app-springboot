package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.BookAdminDTO
import ar.edu.unsam.algo3.dto.BookDTO
import ar.edu.unsam.algo3.dto.SearchDTO
import ar.edu.unsam.algo3.model.Libro
import ar.edu.unsam.algo3.model.Usuario
import ar.edu.unsam.algo3.service.BookService
import ar.edu.unsam.algo3.service.RecommendationService
import ar.edu.unsam.algo3.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class BooksController(
    private val bookService: BookService,
    private val userService: UserService,
    private val recommendationService: RecommendationService
) {

    @ModelAttribute("currentUser")
    fun getCurrentUser(@RequestHeader("Authorization") authorizationReq: String): Usuario {
        return userService.getUserFromBearerToken(authorizationReq)
    }

    @PostMapping("api/booksAdmin")
    fun createBook(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @RequestBody bookAdminDTO: BookAdminDTO
    ) {
        bookService.createBook(bookAdminDTO)
    }

    @DeleteMapping("api/booksAdmin/{idBook}")
    fun deleteBook(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idBook: Int
    ) {
        bookService.deleteBook(idBook)
    }

    @GetMapping("api/books")
    fun getAllBooks(@ModelAttribute("currentUser") currentUser: Usuario): List<BookDTO> {
        val allBooks = bookService.getAllBooks()
        return bookDTOS(allBooks)
    }

    @GetMapping("api/booksAdmin")
    fun getAllBooksAdmin(@ModelAttribute("currentUser") currentUser: Usuario): List<BookAdminDTO> {
        val allBooks = bookService.getAllBooks()
        return bookDTOSAdmin(allBooks)
    }

    @GetMapping("api/booksAdmin/{idBook}")
    fun getBooksAdminById(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idBook: Int
    ) : BookAdminDTO {
        val book = getBookById(idBook)
        return BookAdminDTO.fromBookToDTO(book)
    }

    @PutMapping("api/booksAdmin/{idBook}")
    fun updateBookAdmin(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idBook: Int,
        @RequestBody book: BookAdminDTO
    ) {
        println("LLame al servicio?")
        bookService.updateBook(idBook, book)
    }

    @GetMapping("api/books/to-read")
    fun getBooksToRead(@ModelAttribute("currentUser") currentUser: Usuario): List<BookDTO> {
        val booksToRead = bookService.getBooksToRead(currentUser)
        return bookDTOS(booksToRead)
    }

    @GetMapping("api/books-to-add/to-read")
    fun getBooksToAddToReadingList(@ModelAttribute("currentUser") currentUser: Usuario): List<BookDTO> {
        val booksToAddToReadingList = bookService.getBooksToAddToReadingList(currentUser)
        return bookDTOS(booksToAddToReadingList)
    }

    @GetMapping("api/books-to-add/read")
    fun getBooksToAddToReadList(@ModelAttribute("currentUser") currentUser: Usuario): List<BookDTO> {
        val booksToAddToReadList = bookService.getBooksToAddToReadList(currentUser)
        return bookDTOS(booksToAddToReadList)
    }

    @DeleteMapping("api/books/to-read/{idBook}")
    fun deleteBookToRead(@ModelAttribute("currentUser") currentUser: Usuario, @PathVariable idBook: Int) {
        bookService.deleteBookToRead(currentUser, idBook)
    }

    @PostMapping("api/book/to-read")
    fun addBookToRead(@ModelAttribute("currentUser") currentUser: Usuario, @RequestBody idBook: Int) {
        val book = getBookById(idBook)
        userService.addBookToRead(book, currentUser)
    }

    @GetMapping("api/books/read")
    fun getReadBooks(@ModelAttribute("currentUser") currentUser: Usuario): List<BookDTO> {
        val readBooks = bookService.getReadBooks(currentUser)
        return bookDTOS(readBooks)
    }

    @DeleteMapping("api/books/read/{idBook}")
    fun deleteReadBooks(@ModelAttribute("currentUser") currentUser: Usuario, @PathVariable idBook: Int) {
        bookService.deleteReadBooks(currentUser, idBook)
    }

    @PostMapping("api/books/read")
    fun addReadBook(@ModelAttribute("currentUser") currentUser: Usuario, @RequestBody idBook: Int) {
        val book = getBookById(idBook)
        userService.readBook(book, currentUser)
    }

    @PostMapping("api/filterBook")
    fun filterBook(@ModelAttribute("currentUser") currentUser: Usuario, @RequestBody filter: SearchDTO): List<BookDTO> {
        val books = bookService.filterBooks(filter, currentUser)
        return bookDTOS(books)
    }

    @PostMapping("api/filterBookAdmin")
    fun filterBookAdmin(@ModelAttribute("currentUser") currentUser: Usuario, @RequestBody filter: SearchDTO): List<BookAdminDTO> {
        val books = bookService.filterBooks(filter, currentUser)
        return bookDTOSAdmin(books)
    }

    @GetMapping("api/books-to-add-in-recommendation/{idRecommendation}")
    fun getBooksToRecommendation(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idRecommendation: Int
    ): List<BookDTO> {
        val booksIds = recommendationService.getBooksIds(idRecommendation)
        val booksFilter = bookService.getBooksToAddInRecommendation(booksIds, currentUser)
        return bookDTOS(booksFilter)
    }

    @DeleteMapping("api/delete-book-in-recommendation/{idRecommendation}/{idBook}")
    fun deleteBookRecommendation(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idRecommendation: Int,
        @PathVariable idBook: Int
    ) {
        val book = getBookById(idBook)
        recommendationService.deleteBookInRecommendation(idRecommendation, book)
    }

    @PostMapping("api/add-book-in-recommendation")
    fun addBookRecommendation(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @RequestBody body: RecommendationIdBookId
    ) {
        val idRecommendation = body.idRecommendation
        val idBook = body.idBook
        val book = getBookById(idBook)
        recommendationService.addBookRecommendation(idRecommendation, book, currentUser)
    }

    private fun bookDTOS(allBooks: List<Libro>) = allBooks.map { BookDTO.fromBookToDTO(it) }

    private fun bookDTOSAdmin(allBooks: List<Libro>) = allBooks.map { BookAdminDTO.fromBookToDTO(it) }


    private fun getBookById(idBook: Int) = bookService.getBookById(idBook)

    data class RecommendationIdBookId(val idRecommendation: Int, val idBook: Int)
}
