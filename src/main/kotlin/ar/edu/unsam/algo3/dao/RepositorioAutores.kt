package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.model.Autor

@org.springframework.stereotype.Repository
class RepositorioAutores : Repository<Autor>() {

    override fun search(regex: String): List<Autor> =
        (nombrePartialMatch(regex) + apellidoPartialMatch(regex) + userNameExactMatch(regex)).distinct()

    /*AUX*/
    private fun nombrePartialMatch(regex: String): List<Autor> =
        dataMap.values.filter { it.getNombre().contains(regex, ignoreCase = true) }

    private fun apellidoPartialMatch(regex: String): List<Autor> =
        dataMap.values.filter { it.getApellido().contains(regex, ignoreCase = true) }

    private fun userNameExactMatch(regex: String): List<Autor> =
        dataMap.values.filter { it.getSeudonimo() == regex }

    override fun update(obj: Autor) {}
}