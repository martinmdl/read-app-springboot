@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.readapp.model

import ar.edu.unsam.algo3.model.Autor
import ar.edu.unsam.algo3.model.Lenguaje
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
class AutorSpec : DescribeSpec({
    describe("Testear autor"){
        val autorConsagrado = Autor(
            id = null,
            "pipo",
            "Alegre",
            "yagoo",
            LocalDate.of(1969, 3, 27),
            Lenguaje.es_ES,
            2
        )
        val autorNoConsagrado1 = Autor(
            id = null,
            "pipo",
            "Alegre",
            "yagoo",
            LocalDate.of(2002, 3, 27),
            Lenguaje.es_ES,
            0
        )
        val autorNoConsagrado2 = Autor(
            id = null,
            "marto",
            "rodriguez",
            "marta",
            LocalDate.of(1969, 3, 27),
            Lenguaje.es_ES,
            0
        )

        it("Es consagrado si es mayor de 50 años y tiene por lo menos 1 premio"){
            autorConsagrado.esConsagrado() shouldBe true
        }

        it("No es consagrado si es menor de 50 años aunque tenga premios"){
            autorNoConsagrado1.esConsagrado() shouldBe false
        }

        it("No es consagrado si es mayor de 50 años y no tiene premios"){
            autorNoConsagrado2.esConsagrado() shouldBe false
        }
    }
})