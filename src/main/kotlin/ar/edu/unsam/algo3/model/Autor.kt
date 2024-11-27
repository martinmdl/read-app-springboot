@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Autor(
    override var id: Int? = null,
    private var nombre: String,
    private var apellido: String,
    private var seudonimo: String,
    private var fechaNac: LocalDate,
    var lenguaNativa: Lenguaje,
    private val premios: Int
) : Identidad {
    /*GETTERS*/
    fun getNombre() = nombre
    fun getApellido() = apellido
    fun getSeudonimo() = seudonimo

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun setApellido(apellido: String) {
        this.apellido = apellido
    }

    companion object {
        const val EDAD_CONSAGRACION = 50
        const val CANTIDAD_MINIMA_PREMIOS = 1
    }

    private fun edad(): Long = ChronoUnit.YEARS.between(fechaNac, LocalDate.now())

    fun esConsagrado(): Boolean =
        edad() >= EDAD_CONSAGRACION && premios >= CANTIDAD_MINIMA_PREMIOS
}