@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.model

import java.time.LocalDate

/**
 * Encuentro.
 *
 * @param fecha fecha del encuentro
 * @param duracion duracion en horas del tiempo
 * @param centroDeLectura lugar donde se va realizar el encuentro
 * @property disponible cantidad de cupos restantes disponibles
 *
 */

class Encuentro(
    private val fecha: LocalDate,
    private val duracion: Int,
    private var centroDeLectura: CentroDeLectura
) {
    private var disponible: Int = centroDeLectura.maximaCapacidadPorEncuentro()

    /*ACCESORES*/
    fun fecha(): LocalDate = this.fecha
    fun duracion(): Int = this.duracion
    fun disponible(): Int = this.disponible

    /*METODOS*/

    fun validacionReserva(): Boolean = this.disponibilidad() && !this.vencido()

    fun disponibilidad(): Boolean = this.disponible > 0

    private fun vencido(): Boolean = this.fecha.isBefore(LocalDate.now())

    fun reservarCupo() {
        this.disponible -= 1
    }

}