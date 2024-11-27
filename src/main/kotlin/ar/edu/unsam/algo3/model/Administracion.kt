@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.model

import ar.edu.unsam.algo3.ProcesoAdministracion

class Administracion {

    val usuariosRegistrados: MutableSet<Usuario> = mutableSetOf()
    val autoresRegistrados: MutableSet<Autor> = mutableSetOf()
    val centrosDeLecturaRegistrados: MutableSet<CentroDeLectura> = mutableSetOf()

    fun run(program: List<ProcesoAdministracion>) {
        program.forEach { it.ejecutar(this) }
    }
}