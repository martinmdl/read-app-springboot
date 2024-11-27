@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.model

abstract class PerfilDeRecomendacion(val usuario: Usuario, val nameClass: String) {
    abstract fun validarRecomendacion(recomendacion: Recomendacion): Boolean
}

class Precavido(usuario: Usuario) : PerfilDeRecomendacion(usuario, "Precavido") {
    override fun validarRecomendacion(recomendacion: Recomendacion) =
        librosQuiereLeerRecomendacion(recomendacion) || amigosLeyeron(recomendacion)

    private fun amigosLeyeron(recomendacion: Recomendacion): Boolean =
        usuario.amigos.any { it.librosLeidos.keys.intersect(librosRecomendados(recomendacion)).isNotEmpty() }

    private fun librosQuiereLeerRecomendacion(recomendacion: Recomendacion): Boolean =
        librosRecomendados(recomendacion).intersect(usuario.librosPorLeer).isNotEmpty()

    private fun librosRecomendados(recomendacion: Recomendacion): MutableSet<Libro> = recomendacion.librosRecomendados
}

class Leedor(usuario: Usuario) : PerfilDeRecomendacion(usuario, "Leedor") {
    override fun validarRecomendacion(recomendacion: Recomendacion) = true
}

class Poliglota(usuario: Usuario) : PerfilDeRecomendacion(usuario, "Poliglota") {
    companion object {
        private const val CANT_MINIMA_LENGUAJES = 5
    }

    override fun validarRecomendacion(recomendacion: Recomendacion) =
        cantLenguajes(recomendacion) >= CANT_MINIMA_LENGUAJES

    private fun cantLenguajes(recomendacion: Recomendacion): Int =
        recomendacion.librosRecomendados.flatMap { it.idioma }.toSet().size
}


class Nativista(usuario: Usuario) : PerfilDeRecomendacion(usuario, "Nativista") {
    override fun validarRecomendacion(recomendacion: Recomendacion) = lenguasIguales(recomendacion)
    private fun lenguasIguales(recomendacion: Recomendacion): Boolean =
        recomendacion.librosRecomendados.any { it.autor.lenguaNativa == usuario.lenguaNativa }
}

class Calculador(usuario: Usuario) : PerfilDeRecomendacion(usuario, "Calculador") {
    override fun validarRecomendacion(recomendacion: Recomendacion) =
        cumpleRangoTiempoMinimo(recomendacion) && cumpleRangoTiempoMaximo(recomendacion)

    var rangoMin: Int = 5000
    var rangoMax: Int = 5001
    fun setMax(valor: Int) {
        rangoMax = valor
    }

    fun setMin(valor: Int) {
        rangoMin = valor
    }

    private fun cumpleRangoTiempoMinimo(recomendacion: Recomendacion) =
        recomendacion.tiempoDeLecturaTotal(usuario) >= rangoMin

    private fun cumpleRangoTiempoMaximo(recomendacion: Recomendacion) =
        recomendacion.tiempoDeLecturaTotal(usuario) <= rangoMax
}

class Demandante(usuario: Usuario) : PerfilDeRecomendacion(usuario, "Demandante") {
    companion object {
        private const val VALORACION_MINIMA_PROMEDIO = 3
    }

    override fun validarRecomendacion(recomendacion: Recomendacion) = valoracionAlta(recomendacion)
    private fun valoracionAlta(recomendacion: Recomendacion): Boolean =
        recomendacion.promedioValoraciones() > VALORACION_MINIMA_PROMEDIO
}

class Experimentado(usuario: Usuario) : PerfilDeRecomendacion(usuario, "Experimentado") {
    companion object {
        private const val DIVISOR_MITAD = 2
    }

    override fun validarRecomendacion(recomendacion: Recomendacion) =
        cantidadAutoresConsagrados(recomendacion) >= cantidadAutoresASuperar(recomendacion)

    private fun cantidadAutoresConsagrados(recomendacion: Recomendacion) =
        recomendacion.librosRecomendados.count { it.autor.esConsagrado() }

    private fun cantidadAutoresASuperar(recomendacion: Recomendacion) =
        recomendacion.librosRecomendados.size / DIVISOR_MITAD
}

class Cambiante(usuario: Usuario) : PerfilDeRecomendacion(usuario, "Cambiante") {
    private val edadLimite = 25
    var rangoMin: Int = 10000
    var rangoMax: Int = 15000

    override fun validarRecomendacion(recomendacion: Recomendacion): Boolean {
        return if (!mayoriaEdad()) {
            Leedor(usuario).validarRecomendacion(recomendacion)
        } else {
            val calculador = Calculador(usuario)
            calculador.setMin(rangoMin)
            calculador.setMax(rangoMax)
            calculador.validarRecomendacion(recomendacion)
        }
    }

    private fun mayoriaEdad(): Boolean = usuario.edad() > edadLimite
}

class Combinador(
    usuario: Usuario,
    val perfilesCombinados: MutableSet<PerfilDeRecomendacion> = mutableSetOf()
) : PerfilDeRecomendacion(usuario, "Combinador") {

    override fun validarRecomendacion(recomendacion: Recomendacion) =
        perfilesCombinados.any { it.validarRecomendacion(recomendacion) }

    fun agregarPerfil(perfil: PerfilDeRecomendacion) {
        validarElemento(perfil)
        perfilesCombinados.add(perfil)
    }

    fun removerPerfil(perfil: PerfilDeRecomendacion) {
        perfilesCombinados.remove(perfil)
    }

    private fun validarElemento(perfil: PerfilDeRecomendacion) {
        if (perfil is Combinador) {
            throw Exception("Combinador no puede contener elementos de tipo Combinador")
        }
    }
}