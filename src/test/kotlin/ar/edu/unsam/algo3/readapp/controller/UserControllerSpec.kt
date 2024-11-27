@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.readapp.controller

import ar.edu.unsam.algo3.dao.RepositorioAutores
import ar.edu.unsam.algo3.dao.RepositorioUsuario
import ar.edu.unsam.algo3.dao.UserAuthenticatedRepository
import ar.edu.unsam.algo3.dto.AuthenticatedUser
import ar.edu.unsam.algo3.dto.LoginRequest
import ar.edu.unsam.algo3.dto.UserDTO
import ar.edu.unsam.algo3.model.Autor
import ar.edu.unsam.algo3.model.Lenguaje
import ar.edu.unsam.algo3.model.Usuario
import ar.edu.unsam.algo3.service.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.junit.jupiter.api.BeforeEach
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDate
import kotlin.test.Test
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Dado un controller de usuario")
class UserControllerSpec(@Autowired val mockMvc: MockMvc) {

    val userRepository = RepositorioUsuario()

    val repositoryUserDTO = UserAuthenticatedRepository()

    @Autowired
    lateinit var authService: AuthService

    val objectMapper = ObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

    val author0 = Autor(
        id = null,
        "pipo",
        "Alegre",
        "yagoo",
        LocalDate.of(1990, 3, 27),
        Lenguaje.es_ES,
        2
    )

    val user0 = Usuario(
        nombre = "admin",
        apellido = "torres",
        username = "admin",
        palabrasPorMinuto = 150,
        fechaNac = LocalDate.of(1990, 3, 27),
        direccionEmail = "pipo@yahoo.com",
        amigos = mutableSetOf(),
        librosLeidos = mutableMapOf(),
        autorFavorito = author0,
        recomendacionesPorValorar = mutableSetOf(),
        librosPorLeer = mutableSetOf(),
        lenguaNativa = Lenguaje.es_ES
    )

    val user1 = Usuario(
        nombre = "fran",
        apellido = "torres",
        username = "admin",
        palabrasPorMinuto = 150,
        fechaNac = LocalDate.of(1990, 3, 27),
        direccionEmail = "pipo@yahoo.com",
        amigos = mutableSetOf(),
        librosLeidos = mutableMapOf(),
        autorFavorito = author0,
        recomendacionesPorValorar = mutableSetOf(),
        librosPorLeer = mutableSetOf(),
        lenguaNativa = Lenguaje.es_ES
    )

    val user2 = Usuario(
        nombre = "juan",
        apellido = "torres",
        username = "admin",
        palabrasPorMinuto = 150,
        fechaNac = LocalDate.of(1990, 3, 27),
        direccionEmail = "pipo@yahoo.com",
        amigos = mutableSetOf(),
        librosLeidos = mutableMapOf(),
        autorFavorito = author0,
        recomendacionesPorValorar = mutableSetOf(),
        librosPorLeer = mutableSetOf(),
        lenguaNativa = Lenguaje.es_ES
    )

    val admin = AuthenticatedUser(null, "admin", BCryptPasswordEncoder().encode("admin"), "ADMIN")

    fun getToken(): String {
        val loginReq = LoginRequest("admin",  "admin")
        val token = authService.login(loginReq).token
        return "Bearer " + token
    }

    @BeforeEach
    fun init() {
        userRepository.create(user0)
        userRepository.create(user1)
        userRepository.create(user2)
        repositoryUserDTO.create(admin)
    }

    @Test
    fun `se puede obtener los datos de usuario`() {

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/user")
                .header("Authorization", getToken()))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.lastName").exists())
            .andExpect(jsonPath("$.username").exists())
            .andExpect(jsonPath("$.dateOfBirth").exists())
            .andExpect(jsonPath("$.mail").exists())
            .andExpect(jsonPath("$.wordsPerMinutes").exists())
            .andExpect(jsonPath("$.typeOfReader").exists())
            .andExpect(jsonPath("$.searchCriteria").exists())
            .andExpect(jsonPath("$.minRange").exists())
            .andExpect(jsonPath("$.maxRange").exists())
    }

    @Test
    fun `obtener la lista de todos los usuarios`() {

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/users")
                .header("Authorization", getToken()))
            .andExpect(status().isOk)

        userRepository.getAll().size shouldBe 3
    }

    @Test
    fun `actualizar datos de usuario`() {

        val userJson = UserDTO(
            name = "pepe",
            lastName = "pepe",
            username = "pepe",
            dateOfBirth = LocalDate.of(1990, 3, 27),
            mail = "juancito@gmail.com",
            wordsPerMinutes = 150,
            typeOfReader = "Lector Promedio",
            searchCriteria = mutableListOf("Leedor"),
            minRange = 150,
            maxRange = 180
        )

        mockMvc
            .perform(MockMvcRequestBuilders
                .put("/api/user")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userJson)))
            .andExpect(status().isOk)
    }

    @Test
    fun `agregar un amigo`() {

        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/friends")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(user1.id)))
            .andExpect(status().isOk)
    }

    @Test
    fun `obtener la lista de amigos del usuario`() {

        user0.agregarAmigo(user1)

        mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/friends")
                .header("Authorization", getToken()))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))

        user0.amigos.size shouldBe 1
    }


    @Test
    fun `eliminar un amigo`() {

        mockMvc
            .perform(MockMvcRequestBuilders
                .delete("/api/friends/1")
                .header("Authorization", getToken()))
            .andExpect(status().isOk)
    }
}