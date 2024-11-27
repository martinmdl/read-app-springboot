package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.model.Libro

@org.springframework.stereotype.Repository
class RepositorioLibros : Repository<Libro>() {

    override fun search(regex: String): List<Libro> =
        (matchNombrePartial(regex) + matchApellidoPartial(regex)).toSet().toList()

    /*AUX*/
    private fun matchNombrePartial(regex: String): List<Libro> =
        dataMap.values.filter { it.getNombre().contains(regex, ignoreCase = true) }

    private fun matchApellidoPartial(regex: String): List<Libro> =
        dataMap.values.filter { it.autor.getApellido().contains(regex, ignoreCase = true) }

    override fun update(obj: Libro) {
    }
}
