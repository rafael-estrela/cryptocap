package br.eti.rafaelcouto.cryptocap.application.network.call

import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class ObjectCall<T>(
    private val delegate: Call<Body<Map<String, T>>>,
    private val successType: Type
) : CryptoCall<T>() {

    companion object {
        const val ID_QUERY_PARAM = "id"
    }

    override fun clone() = ObjectCall(delegate.clone(), successType)
    override fun execute(): Response<Result<T>> = throw UnsupportedOperationException()
    override fun isExecuted() = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun isCanceled() = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout  = delegate.timeout()

    @Suppress("TooGenericExceptionCaught")
    override fun enqueue(callback: Callback<Result<T>>) {
        val result: Result<T> = try {
            val response = delegate.execute()
            val body: T? = extractDataFromMap(response.body()?.data.orEmpty())

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

    private fun extractDataFromMap(data: Map<String, T>): T? {
        val queryParam = delegate.request().url.queryParameter(ID_QUERY_PARAM)

        return queryParam?.let { data[it] }
    }
}
