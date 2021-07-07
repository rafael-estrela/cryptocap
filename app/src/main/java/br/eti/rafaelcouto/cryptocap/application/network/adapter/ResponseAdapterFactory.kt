package br.eti.rafaelcouto.cryptocap.application.network.adapter

import br.eti.rafaelcouto.cryptocap.application.network.call.ResponseCall
import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Response
import com.squareup.moshi.Types
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResponseAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        @JvmStatic @JvmName("create") operator fun invoke() = ResponseAdapterFactory()

        const val NON_PARAMETERIZED_ERROR =
            "Response must be parameterized as Response<Foo> or Response<out Foo>"
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val wrapper = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(wrapper) != Response::class.java) return null

        if (wrapper !is ParameterizedType) throw IllegalStateException(NON_PARAMETERIZED_ERROR)

        val actualType = getParameterUpperBound(0, wrapper)

        val callType = Types.newParameterizedType(Body::class.java, actualType)

        return BodyCallAdapter<Any>(callType)
    }

    private class BodyCallAdapter<T>(
        private val responseType: Type
    ) : CallAdapter<Body<T>, Call<Response<T>>> {

        override fun responseType(): Type = responseType
        override fun adapt(call: Call<Body<T>>) = ResponseCall(call, responseType)
    }
}
