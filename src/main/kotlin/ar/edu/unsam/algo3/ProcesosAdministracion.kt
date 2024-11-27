@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3

import ar.edu.unsam.algo3.dao.RepositorioLibros
import ar.edu.unsam.algo3.model.Administracion
import ar.edu.unsam.algo3.model.Autor
import ar.edu.unsam.algo3.utils.Mail
import ar.edu.unsam.algo3.utils.MailSender

abstract class ProcesoAdministracion(private val mailSender: MailSender) {

    protected open lateinit var tipoProceso: String

    fun ejecutar(adm: Administracion) {
        this.enviarMail(mailSender)
        this.accionarProceso(adm)
    }

    private fun enviarMail(mailSender: MailSender) {
        val mail = Mail(
            from = "System@not.com",
            to = "admin@readapp.com.ar",
            subject = "Se ejecuto un proceso",
            content = "Se realizó el proceso: $tipoProceso"
        )
        mailSender.sendMail(mail)
    }

    abstract fun accionarProceso(adm: Administracion)
}

class ActualizacionDeLibro(mailSender: MailSender, private val repoLibros: RepositorioLibros) :
    ProcesoAdministracion(mailSender) {

    override var tipoProceso: String = "ActualizacionDeLibro"

    override fun accionarProceso(adm: Administracion) {
        repoLibros.dataMap.values.forEach { repoLibros.update(it) }
    }
}

class AgregarAutores(mailSender: MailSender, private val autoresPorAgregar: MutableSet<Autor>) :
    ProcesoAdministracion(mailSender) {

    override var tipoProceso: String = "AgregarAutores"

    override fun accionarProceso(adm: Administracion) {
        adm.autoresRegistrados.addAll(autoresPorAgregar)
    }
}

class BorrarUsuarioInactivo(mailSender: MailSender) : ProcesoAdministracion(mailSender) {

    override var tipoProceso: String = "BorrarUsuarioActivo"

    override fun accionarProceso(adm: Administracion) {
        adm.usuariosRegistrados.removeIf { !it.esActivo() }
    }
}

class BorrarCentroDeLecturaExpirados(mailSender: MailSender) : ProcesoAdministracion(mailSender) {

    override var tipoProceso: String = "BorrarCentroDeLecturaExpirados"

    override fun accionarProceso(adm: Administracion) {
        adm.centrosDeLecturaRegistrados.removeIf { it.seVencieronTodasLasFechas() }
    }
}