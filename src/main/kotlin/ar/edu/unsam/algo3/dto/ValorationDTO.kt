package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.model.Valoracion
import java.util.*

data class ValorationDTO(
    val user: String,
    var value: Int,
    var comment: String,
    var date: Date
) {
    companion object {
        fun fromValoration(valoration: Valoracion): ValorationDTO {
            return ValorationDTO(
                user = valoration.autor.nombre,
                value = valoration.valor,
                comment = valoration.comentario,
                date = Date()
            )
        }
    }
}

data class NewValorationDTO(
    var value: Int,
    var comment: String
)
