package br.eti.rafaelcouto.cryptocap.application.network.call

import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class ArrayCall<T>(
    private val delegate: Call<Body<T>>,
    private val successType: Type
) : CryptoCall<T>() {

    override fun clone() = ArrayCall(delegate.clone(), successType)
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
}
