package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.model.CentroDeLectura

@org.springframework.stereotype.Repository
class RepositorioCentroDeLectura : Repository<CentroDeLectura>() {

    override fun search(regex: String): List<CentroDeLectura> = libroExactMatch(regex).distinct()

    /*AUX*/
    private fun libroExactMatch(regex: String): List<CentroDeLectura> =
        dataMap.values.filter { it.getLibroAsignadoALeer().getNombre() == regex }

    override fun update(obj: CentroDeLectura) {}

    fun removeDeprecatedCenters() {
        getAllDeprecated().forEach { delete(it) }
    }

    private fun getAllDeprecated() = dataMap.values.filter { it.seVencieronTodasLasFechas() }
}