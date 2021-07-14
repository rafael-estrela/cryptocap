package br.eti.rafaelcouto.cryptocap.application.network.call

import br.eti.rafaelcouto.cryptocap.application.network.RequestConstants
import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import retrofit2.Call

abstract class CryptoCall<T> : Call<Result<T>> {

    protected fun mapError(exception: Exception): String {
        return exception.message ?: RequestConstants.DEFAULT_ERROR
    }

    protected fun mapError(errorBody: String): String {
        if (errorBody.isEmpty()) return RequestConstants.DEFAULT_ERROR

        val type = Types.newParameterizedType(Body::class.java, Any::class.java)
        val moshi = Moshi.Builder().build()
        val errorObject = moshi.adapter<Body<Any>>(type).fromJson(errorBody)
        return errorObject?.status?.errorMessage ?: RequestConstants.DEFAULT_ERROR
    }
}
