package ar.edu.unsam.algo3

import ar.edu.unsam.algo3.bootstrap.DataInitializer
import ar.edu.unsam.algo3.dao.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ReadAppApplication {

    @Bean
    fun dataInitializer(
        repositorioAutores: RepositorioAutores,
        repositorioLibros: RepositorioLibros,
        repositorioUsuarios: RepositorioUsuario,
        repositorioRecomendaciones: RepositorioRecomendaciones,
        userAuthenticatedRepository: UserAuthenticatedRepository,
        readingCentersRepository: RepositorioCentroDeLectura
    ): DataInitializer {
        val dataInitializer = DataInitializer(
            repositorioAutores,
            repositorioLibros,
            repositorioUsuarios,
            repositorioRecomendaciones,
            userAuthenticatedRepository,
            readingCentersRepository
        )
        dataInitializer.build()
        return dataInitializer
    }
}

fun main(args: Array<String>) {
    runApplication<ReadAppApplication>(*args)
}