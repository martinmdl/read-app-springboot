@file:Suppress("SpellCheckingInspection")
package ar.edu.unsam.algo3.readapp.model
import ar.edu.unsam.algo3.model.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class UsuarioSpec : DescribeSpec({

    describe("Tests de usuario") {

        val autorConsagrado = Autor(
            id = null,
            "pipo",
            "Alegre",
            "yagoo", LocalDate.of(1969, 3, 27), Lenguaje.es_ES, 2
        )

        val autorNoConsagrado = Autor(
            id = null,
            "pipo",
            "Alegre",
            "yagoo", LocalDate.of(2002, 3, 27), Lenguaje.es_ES, 0
        )

        val usuario1 = Usuario(
            nombre = "Diego",
            apellido = "Alegre",
            username = "pipo",
            palabrasPorMinuto = 100,
            fechaNac = LocalDate.of(2000, 4, 23),
            direccionEmail = "pipo@gmail.com",
            autorFavorito = autorNoConsagrado,
            lenguaNativa = Lenguaje.es_ES
        )

        val usuario2 = Usuario(
            nombre = "Diego",
            apellido = "Alegre",
            username = "pipo",
            palabrasPorMinuto = 100,
            fechaNac = LocalDate.of(2000, 4, 23),
            direccionEmail = "pipo@gmail.com",
            autorFavorito = autorNoConsagrado,
            lenguaNativa = Lenguaje.ja_JP
        )

        val libroDesafiante = Libro(
            id = null,
            "HarryPotter",
            "Salamandra",
            800,
            75000,
            true,
            5,
            setOf(Lenguaje.es_ES, Lenguaje.ja_JP),
            10001,
            autorConsagrado,
            Lenguaje.hi_IN
        )

        val libroNoDesafiante = Libro(
            id = null,
            "OnePiece",
            "ivrea",
            150,
            10000,
            false,
            1,
            setOf(Lenguaje.es_ES, Lenguaje.ja_JP),
            10001,
            autorConsagrado,
            Lenguaje.hi_IN
        )
        val recomendacion1 = Recomendacion(
            id = null,
            nombre = "recomendacion1",
            esPrivado = true,
            creador = usuario1,
            librosRecomendados = mutableSetOf(libroDesafiante, libroNoDesafiante),
            descripcion = "no se leer",
            mutableMapOf()
        )

        val recomendacion2 = Recomendacion(
            id = null,
            nombre = "recomendacion2",
            esPrivado = true,
            creador = usuario1,
            librosRecomendados = mutableSetOf(libroNoDesafiante),
            descripcion = "no pude leer",
            mutableMapOf()
        )

        describe("Tiempo de lectura") {

            it("cuánto tarda en leer un libro desafiante") {
                usuario1.tiempoDeLectura(libroDesafiante) shouldBe 1500
            }

            it("cuánto tarda en leer un libro no desafiante") {
                usuario1.tiempoDeLectura(libroNoDesafiante) shouldBe 100
            }
        }

        describe("Tipo lector") {
            it("Por defecto es un lector promedio") {
                usuario1.tipoLector.shouldBeInstanceOf<LectorPromedio>()
            }

            it("El usuario puede variar su tipo de lectura") {
                val lectorNormal = LectorNormal(usuario1)
                usuario1.variarTipoLector(lectorNormal)
                usuario1.tipoLector shouldBe lectorNormal
            }
        }

        describe("Libros") {

            it("El usuario puede agregar libros que quiere leer") {

                usuario1.agregarLibroPorLeer(libroDesafiante)
                usuario1.agregarLibroPorLeer(libroNoDesafiante)

                usuario1.librosPorLeer shouldBe mutableSetOf(libroDesafiante, libroNoDesafiante)
            }


            it("Leo un libro, se agrega a libros leídos y se elimina de libros por leer") {

                usuario1.leerLibro(libroDesafiante)
                usuario1.leerLibro(libroDesafiante)

                usuario1.librosPorLeer shouldBe mutableSetOf(libroNoDesafiante)
                usuario1.librosLeidos shouldBe mutableMapOf(libroDesafiante to 2)
            }


            it("Cuando el usuario agrega a libros por leer un libro ya leído, se captura el error") {
                assertThrows<Exception> { usuario1.agregarLibroPorLeer(libroDesafiante) }
                usuario1.librosLeidos shouldBe mutableMapOf(libroDesafiante to 2)
            }

            it("El usuario puede eliminar libros por leer") {
                usuario1.eliminarLibroPorLeer(libroNoDesafiante)
                usuario1.librosPorLeer shouldBe mutableSetOf()
            }

            it("Leo un libro que no esta en libros por leer, entonces no se elimina de dicha lista") {

                usuario1.leerLibro(libroNoDesafiante)

                usuario1.librosPorLeer shouldBe mutableSetOf()
                usuario1.librosLeidos shouldBe mutableMapOf(libroDesafiante to 2, libroNoDesafiante to 1)
            }
        }

        describe("Autor") {

            it("El usuario puede cambiar su autor favorito") {
                usuario1.autorFavorito shouldBe autorNoConsagrado

                usuario1.variarAutorFavorito(autorConsagrado)

                usuario1.autorFavorito shouldBe autorConsagrado
            }
        }

        describe("Amigos") {

            it("El usuario puede agregar amigos") {
                usuario1.amigos shouldBe mutableSetOf()
                usuario1.agregarAmigo(usuario2)
                usuario1.amigos shouldBe mutableSetOf(usuario2)
            }
            it("El usuario puede eliminar amigos de su lista") {
                usuario1.eliminarAmigo(usuario2)
                usuario1.amigos shouldBe mutableSetOf()
            }
        }

        describe("Recomendaciones") {

            it("El usuario crea una recomendacion"){
                val recomendacion = usuario1.crearRecomendacion( "Recomendacion",mutableSetOf(libroDesafiante), "libro buenardo", true)
                usuario1.listaDeRecomendacionesCreada shouldBe mutableListOf(recomendacion)
            }

            it("Por defecto tiene el perfil de recomendación de Leedor") {
                usuario1.perfilDeRecomendacion.shouldBeInstanceOf<Leedor>()
            }

            it("El usuario puede cambiar el perfil de recomendación") {
                val poliglota = Poliglota(usuario1)
                usuario1.cambiarPerfilDeRecomendacion(poliglota)
                usuario1.perfilDeRecomendacion shouldBe poliglota
            }
        }

        describe("Valoraciones") {

            it("El usuario puede agregar una recomendacion a valorar a una lista") {

                usuario1.agregarRecomendacionPorValorar(recomendacion1)
                usuario1.agregarRecomendacionPorValorar(recomendacion2)

                usuario1.recomendacionesPorValorar shouldBe mutableSetOf(recomendacion2, recomendacion1)
            }

            it("El usuario puede eliminar una recomendacion de la lista") {

                usuario1.eliminarRecomendacionPorValorar(recomendacion1)

                usuario1.recomendacionesPorValorar shouldBe mutableSetOf(recomendacion2)
            }

            it("El usuario valora una recomendación y se agrega a la lista de valoraciones creadas, y se elimina de la lista de recomendaciones por valorar") {
                usuario2.leerLibro(libroDesafiante)
                usuario2.leerLibro(libroNoDesafiante)

                usuario2.valorarRecomendacion(recomendacion1, 2, "test aceptado")

                usuario2.listaDeValoracionesDadas shouldNotBe emptyList<Valoracion>()
                usuario2.recomendacionesPorValorar shouldBe mutableSetOf()
            }
        }
    }
})