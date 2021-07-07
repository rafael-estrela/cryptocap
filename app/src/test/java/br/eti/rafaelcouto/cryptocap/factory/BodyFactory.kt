package br.eti.rafaelcouto.cryptocap.factory

import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Status

object BodyFactory {

    val successStatus get() = Status(null)
    val errorStatus get() = Status("dummy error message")

    fun <T> successBody(data: T) = Body(successStatus, data)
    fun <T> errorBody() = Body<T>(errorStatus, null)
}
