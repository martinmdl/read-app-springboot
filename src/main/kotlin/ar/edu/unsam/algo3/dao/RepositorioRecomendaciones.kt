package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.model.Recomendacion


@org.springframework.stereotype.Repository
class RepositorioRecomendaciones : Repository<Recomendacion>() {

    override fun search(regex: String): List<Recomendacion> =
        ((apellidoExactMatch(regex) + nombrePartialMatch(regex) + reseniaPartialMatch(regex)).distinct() + namePartialMatch(
            regex
        )).toSet().toList()

    /*AUX*/
    private fun apellidoExactMatch(regex: String): List<Recomendacion> =
        dataMap.values.filter { it.creador.apellido == regex }

    private fun nombrePartialMatch(regex: String): List<Recomendacion> =
        dataMap.values.filter { recommendation ->
            recommendation.librosRecomendados.any { it.getNombre().contains(regex, ignoreCase = true) }
        }

    private fun reseniaPartialMatch(regex: String): List<Recomendacion> =
        dataMap.values.filter { recommendation ->
            recommendation.valoraciones.values.any { it.comentario.contains(regex, ignoreCase = true) }
        }

    private fun namePartialMatch(regex: String): List<Recomendacion> =
        dataMap.values.filter { it.nombre.contains(regex, ignoreCase = true) }

    override fun update(obj: Recomendacion) {}

}