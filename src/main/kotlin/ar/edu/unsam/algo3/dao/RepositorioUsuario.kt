package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.model.Usuario

@org.springframework.stereotype.Repository
class RepositorioUsuario : Repository<Usuario>() {

    override fun search(regex: String): List<Usuario> =
        (matchNombrePartial(regex) + matchApellidoParcial(regex) + matchUserNameCompleto(regex)).distinct()

    /*AUX*/
    private fun matchNombrePartial(regex: String): List<Usuario> =
        dataMap.values.filter { it.nombre.contains(regex, ignoreCase = true) }

    private fun matchApellidoParcial(regex: String): List<Usuario> =
        dataMap.values.filter { it.apellido.contains(regex, ignoreCase = true) }

    private fun matchUserNameCompleto(regex: String): List<Usuario> =
        dataMap.values.filter { it.username == regex }

    override fun update(obj: Usuario) {}


    fun removeInactiveUsers() {
        getAllInactives().forEach { delete(it) }
    }

    private fun getAllInactives() = dataMap.values.filter { !it.esActivo() }
}