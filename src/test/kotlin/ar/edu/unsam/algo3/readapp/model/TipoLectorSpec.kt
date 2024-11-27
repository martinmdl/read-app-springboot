@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.readapp.model

import ar.edu.unsam.algo3.model.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TipoLectorSpec : DescribeSpec({
    describe("Test de tiempos de lectura") {

        val autorConsagrado = Autor(
            id= null,
            "pipo",
            "Alegre",
            "yagoo", LocalDate.of(1969, 3, 27), Lenguaje.es_ES, 2
        )
        val autorNoConsagrado = Autor(
            id= null,
            "pipo",
            "Alegre",
            "yagoo", LocalDate.of(2002, 3, 27), Lenguaje.es_ES, 0
        )
        val usuario = Usuario(
            id= null,
            nombre = "Diego",
            apellido = "Alegre",
            username = "pipo",
            palabrasPorMinuto = 100,
            fechaNac = LocalDate.of(2000, 4, 23),
            direccionEmail = "pipo@gmail.com",
            autorFavorito = autorNoConsagrado,
            lenguaNativa = Lenguaje.es_ES
        )

        val libroDesafiante = Libro(
            id= null,
            "HarryPotter",
            "Salamandra",
            800,
            75000,
            true,
            1,
            setOf(Lenguaje.es_ES, Lenguaje.ja_JP, Lenguaje.fr_FR, Lenguaje.ru_RU, Lenguaje.hi_IN),
            1,
            autorConsagrado,
            Lenguaje.hi_IN
        )

        val libroNoDesafiante = Libro(
            id= null,
            "HarryPotter",
            "Salamandra",
            300,
            500,
            false,
            1,
            setOf(Lenguaje.es_ES, Lenguaje.ja_JP, Lenguaje.fr_FR, Lenguaje.ru_RU, Lenguaje.hi_IN),
            1,
            autorConsagrado,
            Lenguaje.hi_IN
        )

        val libroBestSeller = Libro(
            id= null,
            "Skander",
            "Salamandra",
            300,
            75000,
            false,
            5,
            setOf(Lenguaje.es_ES, Lenguaje.ja_JP, Lenguaje.fr_FR, Lenguaje.ru_RU, Lenguaje.hi_IN),
            1,
            autorNoConsagrado,
            Lenguaje.hi_IN
        )

        val libroNotBestSeller = Libro(
            id= null,
            "jojos",
            "Salamandra",
            300,
            75000,
            false,
            1,
            setOf(Lenguaje.ja_JP),
            1,
            autorNoConsagrado,
            Lenguaje.hi_IN
        )

        describe("Dado un lector promedio") {

            it("Con un libro desafiante, disminuye su velocidad de lectura") {
                usuario.tiempoDeLectura(libroDesafiante) shouldBe 1500

            }
            it("Con un libro no desafiante, no cambia su velocidad de lectura") {
                usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 5
            }
        }

        describe("Dado un lector normal") {
            val lectorNormal = LectorNormal(usuario)
            usuario.variarTipoLector(lectorNormal)

            it("Con un libro desafiante, no cambia su velocidad de lectura") {
                usuario.tiempoDeLectura(libroDesafiante) shouldBe 750
            }
        }

        describe("Dado un lector ansioso") {
            val lectorAnsioso = LectorAnsioso(usuario)
            usuario.variarTipoLector(lectorAnsioso)

            it("Con un libro best seller, su tiempo de lectura disminuye a la mitad") {
                usuario.tiempoDeLectura(libroBestSeller) shouldBe 375

            }
            it("Con un libro no best seller, su tiempo de lectura disminuye un 20%") {
                usuario.tiempoDeLectura(libroNotBestSeller) shouldBe 600
            }
        }

        describe("Dado un lector fanático") {
            val lectorFanatico = LectorFanatico(usuario)
            usuario.variarTipoLector(lectorFanatico)
            usuario.variarAutorFavorito(autorConsagrado)

            // importante = libro.autor == usuario.autorFavorito && !(usuario.librosLeidos.keys.contains(libro))
            describe("Con libro imporante") {

                it("Siendo un libro corto, se le suma 2 minutos por página") {
                    usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 605
                }

                it("Siendo un libro largo, se le suma 2 minutos por página hasta 600 páginas, luego se le suma 1 minuto por página") {
                    usuario.tiempoDeLectura(libroDesafiante) shouldBe 2900
                }
            }

            usuario.variarAutorFavorito(autorNoConsagrado)

            describe("Con libro no importante") {
                it("Independientemente del largo del libro, no se le suma tiempo adicional") {
                    usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 5
                }
            }
        }

        describe("Dado un lector recurrente") {
            val lectorRecurrente = LectorRecurrente(usuario)
            usuario.variarTipoLector(lectorRecurrente)

            it("Libro no leído, no disminuye velocidad") {
                usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 5
            }

            usuario.leerLibro(libroNoDesafiante)

            it("Libro leído 1 vez, disminuye en 1% la velocidad de lectura") {
                usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 500 / (100 * 0.99)
            }

            // DEUDA TECNICA
            usuario.leerLibro(libroNoDesafiante)
            usuario.leerLibro(libroNoDesafiante)
            usuario.leerLibro(libroNoDesafiante)
            usuario.leerLibro(libroNoDesafiante)

            it("Libro leído mas de 5 veces, no disminuye mas que 4% la velocidad de lectura") {
                usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 500 / (100 * 0.96)
            }
        }
    }
})

