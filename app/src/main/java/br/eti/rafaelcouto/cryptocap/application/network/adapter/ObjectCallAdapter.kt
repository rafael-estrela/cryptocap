package br.eti.rafaelcouto.cryptocap.application.network.adapter

import br.eti.rafaelcouto.cryptocap.application.network.call.ObjectCall
import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ObjectCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<Body<Map<String, T>>, Call<Result<T>>> {

    override fun responseType() = responseType
    override fun adapt(call: Call<Body<Map<String, T>>>) = ObjectCall(call, responseType)
}
