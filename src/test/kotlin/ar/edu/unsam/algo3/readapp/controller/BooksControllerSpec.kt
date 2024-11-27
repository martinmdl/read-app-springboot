@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.readapp.controller


import ar.edu.unsam.algo3.dao.RepositorioLibros
import ar.edu.unsam.algo3.dao.RepositorioUsuario
import ar.edu.unsam.algo3.dto.LoginRequest
import ar.edu.unsam.algo3.service.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import kotlin.test.Test
import org.springframework.http.MediaType

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Dado un controller de libro")
class BooksControllerSpec(@Autowired val mockMvc: MockMvc) {

    @Autowired
    lateinit var booksRepository: RepositorioLibros

    @Autowired
    lateinit var userRepository: RepositorioUsuario

    @Autowired
    lateinit var authService: AuthService

    val objectMapper = ObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

    fun getToken(username: String, password: String): String {
        val loginReq = LoginRequest(username, password)
        val token = authService.login(loginReq).token
        return "Bearer " + token
    }

    @Test
    fun `obtener todos los libros`() {

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/books")
                .header("Authorization", getToken("admin", "admin")))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(8))
    }

    @Test
    fun `obtener todos los libros para leer del usuario1`(){

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/books/to-read")
                .header("Authorization", getToken("valen", "valen")))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
    }

//    @Test
//    fun `obtener todos los libros que puede agregar a leer o leidos del usuario0`(){
//
//        mockMvc
//            .perform(MockMvcRequestBuilders
//                .get("/api/books/books-to-add")
//                .header("Authorization", getToken("marto", "marto")))
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.length()").value(5))
//    }

    @Test
    fun `eliminar el libro a leer con id 3 de usuario2`(){

        mockMvc
            .perform(MockMvcRequestBuilders
                .delete("/api/books/to-read/3")
                .header("Authorization", getToken("valen", "valen")))
            .andExpect(status().isOk)
    }

//    @Test
//    fun `agregar libro 1 a leer al usuario0`(){
//
//        mockMvc
//            .perform(MockMvcRequestBuilders
//                .post("/api/books/to-read")
//                .header("Authorization", getToken("admin", "admin"))
//                .content("4"))
//            .andExpect(status().isOk)
//    }

    @Test
    fun `obtener todos los libros leidos del usuario0`(){

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/books/read")
                .header("Authorization", getToken("admin", "admin")))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(4))
    }

}