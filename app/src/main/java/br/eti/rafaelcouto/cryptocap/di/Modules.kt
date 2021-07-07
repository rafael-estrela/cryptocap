package br.eti.rafaelcouto.cryptocap.di

import br.eti.rafaelcouto.cryptocap.BuildConfig
import br.eti.rafaelcouto.cryptocap.application.network.interceptor.HeaderInterceptor
import br.eti.rafaelcouto.cryptocap.application.network.RequestConstants
import br.eti.rafaelcouto.cryptocap.application.network.adapter.ResultAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Modules {

    private val network = module {
        single<Interceptor> { HeaderInterceptor() }
        single<Converter.Factory> { MoshiConverterFactory.create() }
        single<CallAdapter.Factory> { ResultAdapterFactory.invoke() }

        single {
            OkHttpClient.Builder()
                .addInterceptor(get<Interceptor>())
                .callTimeout(RequestConstants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()
        }

        single {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(get())
                .addCallAdapterFactory(get())
                .client(get())
                .build()
        }
    }

    private val api = module {

    }

    private val repository = module {

    }

    private val useCase = module {

    }

    private val mapper = module {

    }

    private val viewModel = module {

    }

    val all = listOf(network, api, repository, useCase, mapper, viewModel)
}
