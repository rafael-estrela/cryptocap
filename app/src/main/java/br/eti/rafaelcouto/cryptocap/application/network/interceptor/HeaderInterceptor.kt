package br.eti.rafaelcouto.cryptocap.application.network.interceptor

import br.eti.rafaelcouto.cryptocap.BuildConfig
import br.eti.rafaelcouto.cryptocap.application.network.RequestConstants
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(RequestConstants.API_KEY_HEADER, BuildConfig.API_KEY)
            .build()

        return chain.proceed(request)
    }
}
