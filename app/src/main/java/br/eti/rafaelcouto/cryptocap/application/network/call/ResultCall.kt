package br.eti.rafaelcouto.cryptocap.application.network.call

import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class ResultCall<T>(
    private val delegate: Call<Body<T>>,
    private val successType: Type
) : Call<Result<T>> {

    override fun clone() = ResultCall(delegate.clone(), successType)
    override fun execute(): Response<Result<T>> = throw UnsupportedOperationException()
    override fun isExecuted() = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun isCanceled() = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()

    override fun enqueue(callback: Callback<Result<T>>) {
        val result: Result<T> = try {
            val response = delegate.execute()
            val body: T? = response.body()?.data

            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                val error = mapError(response.errorBody()?.string().orEmpty())
                Result.error(error)
            }
        } catch (e: Exception) {
            val error = mapError(e)
            Result.error(error)
        }

        callback.onResponse(this, Response.success(result))
    }

    private fun mapError(exception: Exception): String {
        return exception.message.orEmpty()
    }

    private fun mapError(errorBody: String): String {
        if (errorBody.isEmpty()) return errorBody

        val type = Types.newParameterizedType(Body::class.java, Any::class.java)
        val moshi = Moshi.Builder().build()
        val errorObject = moshi.adapter<Body<Any>>(type).fromJson(errorBody)
        return errorObject?.status?.errorMessage.orEmpty()
    }
}
