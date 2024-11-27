@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.dao.*
import ar.edu.unsam.algo3.dto.AuthenticatedUser
import ar.edu.unsam.algo3.model.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate

class DataInitializer(
    private val repositorioAutores: RepositorioAutores,
    private val repositorioLibros: RepositorioLibros,
    private val repositorioUsuarios: RepositorioUsuario,
    private val repositorioRecomendaciones: RepositorioRecomendaciones,
    private val repositoryUserDTO: UserAuthenticatedRepository,
    private val repositoryReadingCenters: RepositorioCentroDeLectura
) {

    // Crear autor
    val autor0 = Autor(
        id = null,
        "pipo",
        "Alegre",
        "yagoo",
        LocalDate.of(1990, 3, 27),
        Lenguaje.es_ES,
        2
    )

    val autor1 = Autor(
        id = null,
        "roberto",
        "perez",
        "rober",
        LocalDate.of(1995, 3, 27),
        Lenguaje.es_ES,
        3
    )

    val autor2 = Autor(
        id = null,
        "juan",
        "gomez",
        "juang",
        LocalDate.of(1985, 5, 15),
        Lenguaje.es_ES,
        4
    )

    val autor3 = Autor(
        id = null,
        "maria",
        "lopez",
        "marial",
        LocalDate.of(1992, 7, 20),
        Lenguaje.es_ES,
        5
    )

    val autor4 = Autor(
        id = null,
        "pedro",
        "martinez",
        "pedrom",
        LocalDate.of(1988, 11, 30),
        Lenguaje.es_ES,
        6
    )

    val autor5 = Autor(
        id = null,
        "ana",
        "garcia",
        "anag",
        LocalDate.of(1993, 2, 10),
        Lenguaje.es_ES,
        7
    )

    val autor6 = Autor(
        id = null,
        "luis",
        "fernandez",
        "luisf",
        LocalDate.of(1980, 8, 25),
        Lenguaje.es_ES,
        8
    )

    val autor7 = Autor(
        id = null,
        "carla",
        "rodriguez",
        "carlar",
        LocalDate.of(1995, 12, 5),
        Lenguaje.es_ES,
        9
    )

    // Crear libros
    val harryPotter0 = Libro(
        id = null,
        "HarryPotter 1",
        "Salamandra",
        250,
        140000,
        true,
        5,
        setOf(Lenguaje.es_ES, Lenguaje.ja_JP),
        10001,
        autor0,
        Lenguaje.es_ES
    )
    val harryPotter1 = Libro(
        id = null,
        "HarryPotter 2",
        "Salamandra",
        800,
        100000,
        true,
        5,
        setOf(Lenguaje.es_ES, Lenguaje.ja_JP),
        10001,
        autor0,
        Lenguaje.es_ES

    )
    val harryPotter2 = Libro(
        id = null,
        "\n" +
                "I'm A High School Boy and a Successful Light Novel Author, But I'm Being Strangled By A Female Classmate Who's A Voice Actress And Is Younger Than Me",
        "Salamandra",
        800,
        180000,
        true,
        5,
        setOf(Lenguaje.es_ES, Lenguaje.ja_JP),
        10001,
        autor0,
        Lenguaje.es_ES

    )
    val harryPotter3 = Libro(
        id = null,
        "HarryPotter 4",
        "Salamandra",
        800,
        90000,
        true,
        5,
        setOf(Lenguaje.es_ES, Lenguaje.ja_JP),
        10001,
        autor0,
        Lenguaje.es_ES

    )
    val harryPotter4 = Libro(
        id = null,
        "HarryPotter 5",
        "Salamandra",
        800,
        70000,
        true,
        5,
        setOf(Lenguaje.es_ES, Lenguaje.ja_JP),
        10001,
        autor0,
        Lenguaje.es_ES

    )

    // Crear libros con diferentes autores
    val libro1 = Libro(
        id = null,
        "Libro 1",
        "Editorial 1",
        300,
        50000,
        false,
        4,
        setOf(Lenguaje.es_ES),
        10002,
        autor1,
        Lenguaje.es_ES

    )

    val libro2 = Libro(
        id = null,
        "Libro 2",
        "Editorial 2",
        1,
        1,
        true,
        1,
        setOf(Lenguaje.es_ES),
        1,
        autor2,
        Lenguaje.es_ES

    )

    val libro3 = Libro(
        id = null,
        "Libro 3",
        "Editorial 3",
        15,
        100,
        false,
        1,
        setOf(Lenguaje.es_ES),
        26,
        autor3,
        Lenguaje.es_ES

    )

    // Crear usuario que es creador de las recomendaciones
    val usuario0 = Usuario(
        nombre = "admin",
        apellido = "torres",
        username = "admin",
        palabrasPorMinuto = 150,
        fechaNac = LocalDate.of(1990, 3, 27),
        direccionEmail = "pipo@yahoo.com",
        amigos = mutableSetOf(),
        librosLeidos = mutableMapOf(harryPotter0 to 1, harryPotter1 to 1, harryPotter2 to 1, harryPotter3 to 1),
        autorFavorito = autor1,
        recomendacionesPorValorar = mutableSetOf(),
        librosPorLeer = mutableSetOf(),
        lenguaNativa = Lenguaje.es_ES
    )

    val usuario1 = Usuario(
        nombre = "valen",
        apellido = "caseres",
        username = "valen",
        palabrasPorMinuto = 200,
        fechaNac = LocalDate.of(1990, 3, 27),
        direccionEmail = "pipo@yahoo.com",
        amigos = mutableSetOf(),
        librosLeidos = mutableMapOf(harryPotter1 to 1, harryPotter2 to 1, harryPotter4 to 1),
        autorFavorito = autor1,
        librosPorLeer = mutableSetOf(harryPotter3, harryPotter0),
        recomendacionesPorValorar = mutableSetOf(),
        lenguaNativa = Lenguaje.es_ES,
    )

    val usuario2 = Usuario(
        nombre = "fran",
        apellido = "fernandez",
        username = "fran",
        palabrasPorMinuto = 250,
        fechaNac = LocalDate.of(1990, 3, 27),
        direccionEmail = "pipo@yahoo.com",
        amigos = mutableSetOf(),
        librosLeidos = mutableMapOf(
            harryPotter0 to 1,
            harryPotter1 to 1,
            harryPotter2 to 1,
            harryPotter3 to 1,
            harryPotter4 to 1
        ),
        autorFavorito = autor1,
        recomendacionesPorValorar = mutableSetOf(),
        librosPorLeer = mutableSetOf(),
        lenguaNativa = Lenguaje.es_ES
    )

    val usuario3 = Usuario(
        nombre = "marto",
        apellido = "Darwin",
        username = "marto",
        palabrasPorMinuto = 500,
        fechaNac = LocalDate.of(1990, 3, 27),
        direccionEmail = "pipo@yahoo.com",
        amigos = mutableSetOf(),
        librosLeidos = mutableMapOf(),
        autorFavorito = autor1,
        recomendacionesPorValorar = mutableSetOf(),
        librosPorLeer = mutableSetOf(),
        lenguaNativa = Lenguaje.es_ES
    )

    val usuario4 = Usuario(
        nombre = "tincho",
        apellido = "fentanilo",
        username = "tinchobtt",
        palabrasPorMinuto = 500,
        fechaNac = LocalDate.of(1990, 3, 27),
        direccionEmail = "pipo@yahoo.com",
        amigos = mutableSetOf(),
        librosLeidos = mutableMapOf(),
        autorFavorito = autor1,
        recomendacionesPorValorar = mutableSetOf(),
        librosPorLeer = mutableSetOf(),
        lenguaNativa = Lenguaje.es_ES
    )

    val recomendacion0 =
        usuario0.crearRecomendacion(
            "Recomendacion 0",
            mutableSetOf(harryPotter1),
            "Recomendacion Privada 0 de Admin",
            true
        )

    val recomendacion1 =
        usuario0.crearRecomendacion(
            "Recomendacion 1",
            mutableSetOf(harryPotter1, harryPotter2),
            "Recomendacion Publica 1 de Admin",
            false
        )

    val recomendacion2 = usuario0.crearRecomendacion(
        "Recomendacion 2",
        mutableSetOf(harryPotter0, harryPotter1, harryPotter2, harryPotter3, harryPotter4),
        "Recomendacion Publica 2 de Admin",
        false
    )

    val recomendacion3 =
        usuario2.crearRecomendacion(
            "Recomendacion 3",
            mutableSetOf(harryPotter0, harryPotter1),
            "Recomendacion Publica 0 de Fran",
            false
        )

    val recomendacion4 =
        usuario2.crearRecomendacion(
            "Recomendacion 4",
            mutableSetOf(harryPotter3, harryPotter4),
            "Recomendacion Privada 1 de Fran",
            true
        )

    // Crear la credencial del administrador
    val admin = AuthenticatedUser(null, "admin", BCryptPasswordEncoder().encode("admin"), "ADMIN")

    //Crear la credencial de los usuarios
    val valenDTO1 = AuthenticatedUser(null, "valen", BCryptPasswordEncoder().encode("valen"), "USER")
    val franDTO2 = AuthenticatedUser(null, "fran", BCryptPasswordEncoder().encode("fran"), "USER")
    val martoDTO3 = AuthenticatedUser(null, "marto", BCryptPasswordEncoder().encode("marto"), "USER")
    val tinchoDTO5 = AuthenticatedUser(null, "tincho", BCryptPasswordEncoder().encode("tincho"), "USER")

    val readingCenter1 = Particular(
        nombreDeCentroDeLectura = "unsam",
        direccion = "Miguel",
        libroAsignadoALeer = harryPotter0,
        costoDeReserva = 500.0,
        conjuntoDeEncuentros = mutableSetOf(),
        capacidadMaximaFijada = 20,
        porcentajeMinimo = 40.0
    )

    fun build() {

        // CREAR AUTORES
        val autores = mutableListOf(autor0, autor1, autor2, autor3, autor4, autor5, autor6, autor7)
        repositorioAutores.createAll(autores)

        // CREAR LIBROS
        val libros = mutableListOf(harryPotter0, harryPotter1, harryPotter2, harryPotter3, harryPotter4, libro1, libro2, libro3)
        repositorioLibros.createAll(libros)

        // CREAR USUARIOS
        val usuarios = mutableListOf(usuario0, usuario1, usuario2, usuario3, usuario4)
        repositorioUsuarios.createAll(usuarios)

        // CREAR RECOMENDACIONES
        val recomendaciones =
            mutableListOf(recomendacion0, recomendacion1, recomendacion2, recomendacion3, recomendacion4)
        repositorioRecomendaciones.createAll(recomendaciones)

        // CREAR USUARIOS AUTENTICADOS
        val usuariosAuth = mutableListOf(admin, valenDTO1, franDTO2, martoDTO3, tinchoDTO5)
        repositoryUserDTO.createAll(usuariosAuth)

        // CREAR CENTROS DE LECTURA
        val readingCenters = mutableListOf(readingCenter1)
        repositoryReadingCenters.createAll(readingCenters)

        // AGREGAR AMIGOS
        usuario1.agregarAmigo(usuario0)
        usuario3.agregarAmigo(usuario2)

        // LEER LIBROS
        usuario1.leerLibro(harryPotter1)
        usuario2.leerLibro(harryPotter1)
        usuario3.leerLibro(harryPotter1)
        usuario1.valorarRecomendacion(recomendacion0, 5, "buenisimouu")
        usuario2.valorarRecomendacion(
            recomendacion0,
            3,
            "buenardouuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu"
        )

        // VALORAR RECOMENDACIONES
        usuario3.valorarRecomendacion(recomendacion0, 2, "malisimo")

        // PERFILES DE USUARIO (LEEDOR POR DEFECTO)
        usuario0.cambiarPerfilDeRecomendacion(Demandante(usuario0))
        usuario1.cambiarPerfilDeRecomendacion(Cambiante(usuario1))
        usuario2.cambiarPerfilDeRecomendacion(
            Combinador(
                usuario1,
                perfilesCombinados = mutableSetOf(Leedor(usuario1), Cambiante(usuario1))
            )
        )

        // AGREGAR LIBROS POR LEER
        usuario0.agregarLibroPorLeer(harryPotter4)
    }
}


