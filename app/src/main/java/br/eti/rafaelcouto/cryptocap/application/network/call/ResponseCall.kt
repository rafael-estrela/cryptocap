package br.eti.rafaelcouto.cryptocap.application.network.call

import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Response
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import java.lang.reflect.Type

class ResponseCall<T>(
    private val delegate: Call<Body<T>>,
    private val successType: Type
) : Call<Response<T>> {

    override fun clone() = ResponseCall(delegate.clone(), successType)
    override fun execute(): retrofit2.Response<Response<T>> = throw UnsupportedOperationException()
    override fun isExecuted() = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun isCanceled() = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()

    override fun enqueue(callback: Callback<Response<T>>) {
        try {
            val response = delegate.execute()

            if (response.isSuccessful) {
                callback.onResponse(this, retrofit2.Response.success(Response.success(response.body()?.data!!)))
            } else {
                val error = mapError(response.errorBody()?.string().orEmpty())
                callback.onResponse(this, retrofit2.Response.success(Response.error(error)))
            }
        } catch (e: Exception) {
            val error = mapError(e)
            callback.onResponse(this, retrofit2.Response.success(Response.error(error)))
        }
    }

    private fun mapError(exception: Exception): String {
        return exception.message.orEmpty()
    }

    private fun mapError(errorBody: String): String {
        val type = Types.newParameterizedType(Body::class.java, Any::class.java)
        val moshi = Moshi.Builder().build()
        val errorObject = moshi.adapter<Body<Any>>(type).fromJson(errorBody)
        return errorObject?.status?.errorMessage.orEmpty()
    }
}
