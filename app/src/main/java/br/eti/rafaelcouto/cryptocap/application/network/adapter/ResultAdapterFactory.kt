package br.eti.rafaelcouto.cryptocap.application.network.adapter

import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import com.squareup.moshi.Types
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        @JvmStatic @JvmName("create") operator fun invoke() = ResultAdapterFactory()

        const val NON_PARAMETERIZED_ERROR =
            "Response must be parameterized as Result<Foo> or Result<out Foo>"
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (returnType is ParameterizedType) {
            val wrapper = getParameterUpperBound(0, returnType)

            if (getRawType(wrapper) == Result::class.java) {
                if (wrapper !is ParameterizedType) throw IllegalStateException(NON_PARAMETERIZED_ERROR)

                val actualType = getParameterUpperBound(0, wrapper)

                val callType = Types.newParameterizedType(Body::class.java, actualType)

                return ResultCallAdapter<Any>(callType)
            }
        }

        return null
    }
}
