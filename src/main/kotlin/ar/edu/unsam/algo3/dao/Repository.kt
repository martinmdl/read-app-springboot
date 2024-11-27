@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.dao

import ar.edu.unsam.algo3.exceptions.Businessexception
import ar.edu.unsam.algo3.model.Identidad
import org.springframework.stereotype.Repository
import java.util.*

@Repository
abstract class Repository<T : Identidad> {
    private var currentId: Int = 1
    val dataMap: SortedMap<Int, T> = sortedMapOf()

    fun create(obj: T) {
        if (obj.id == null) {
            dataMap[currentId] = obj
            obj.id = currentId
            currentId++
        } else {
            throw Businessexception("$obj ya creado")
        }
    }

    fun delete(obj: T) {
        try {
            val objeto = getById(obj.id!!)
            dataMap.remove(objeto.id)
            obj.id = null
        } catch (e: NullPointerException) {
            throw Businessexception("No se puede eliminar")
        }

    }

    fun getById(id: Int): T {
        try {
            val user = dataMap[id]
            user?.id = id
            return user!!
        } catch (e: NullPointerException) {
            throw Businessexception("No es necesario actualizar el id a $id")
        }

    }

    abstract fun update(obj: T)

    abstract fun search(regex: String): List<T>
    fun createAll(list: List<T>) {
        list.forEach { create(it) }
    }

    fun getAll(): List<T> = dataMap.values.toList()
}


