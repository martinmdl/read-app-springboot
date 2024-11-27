package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.dao.RepositorioCentroDeLectura
import ar.edu.unsam.algo3.model.CentroDeLectura
import org.springframework.stereotype.Service

@Service
class ReadingCentersService(
    private val repositoryCenters: RepositorioCentroDeLectura
) {

    fun getAllCenters(): List<CentroDeLectura> = repositoryCenters.getAll()

    fun getCentersAmount(): Int = getAllCenters().size

    fun deleteInactiveCenters() { repositoryCenters.removeDeprecatedCenters() }

    fun getAllInactiveCenters(): Int = repositoryCenters.getAll().filter { it.seVencieronTodasLasFechas() }.size
}