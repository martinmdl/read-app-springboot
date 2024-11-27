package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.model.Autor
import ar.edu.unsam.algo3.model.Usuario
import ar.edu.unsam.algo3.service.AuthorService
import ar.edu.unsam.algo3.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class AuthorController(
    private val userService: UserService,
    private val authorService: AuthorService
) {
    @ModelAttribute("currentUser")
    fun getCurrentUser(@RequestHeader("Authorization") authorizationReq: String): Usuario {
        return userService.getUserFromBearerToken(authorizationReq)
    }

    @GetMapping("api/author")
    fun getAllAuthors(@ModelAttribute("currentUser") currentUser: Usuario): List<AuthorDTO> {
        val allAuthors = authorService.getAllAuthors()
        return authorDto(allAuthors)
    }

    @GetMapping("api/author/{id}")
    fun getAuthor(@ModelAttribute("currentUser") currentUser: Usuario, @PathVariable("id") id: Int): AuthorDetailsDTO {
        val author = authorService.getAuthor(id)
        return AuthorDetailsDTO.fromAuthorToDTO(author)
    }

    @DeleteMapping("api/authorDelete/{id}")
    fun deleteAuthor(@ModelAttribute("currentUser") currentUser: Usuario, @PathVariable id: Int) {
        authorService.deleteAuthor(id)
    }

    @PutMapping("api/authorEdition/{id}")
    fun authorEdition(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable id: Int,
        @RequestBody authorDetailsDTO: AuthorDetailsDTO
    ) {
        val author = authorService.getAuthor(id)
        authorService.updateAuthor(author, authorDetailsDTO)
    }

    @PostMapping("api/newAuthor")
    fun createAuthor(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @RequestBody authorDetailsDTO: AuthorDetailsDTO
    ) {
        authorService.createAuthor(authorDetailsDTO)
    }

    @PostMapping("api/filterAuthor")
    fun filterAuthor(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @RequestBody filter: SearchDTO
    ): List<AuthorDTO> {
        val authors = authorService.filterAuthor(filter)
        return authorDto(authors)
    }

    private fun authorDto(allAuthors: List<Autor>) = allAuthors.map { AuthorDTO.fromAuthorToDTO(it) }
}