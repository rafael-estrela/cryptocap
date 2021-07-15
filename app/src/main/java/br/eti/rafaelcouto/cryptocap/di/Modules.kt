package br.eti.rafaelcouto.cryptocap.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import br.eti.rafaelcouto.cryptocap.BuildConfig
import br.eti.rafaelcouto.cryptocap.application.network.RequestConstants
import br.eti.rafaelcouto.cryptocap.application.network.adapter.ResultAdapterFactory
import br.eti.rafaelcouto.cryptocap.application.network.interceptor.HeaderInterceptor
import br.eti.rafaelcouto.cryptocap.data.api.CryptoDetailsApi
import br.eti.rafaelcouto.cryptocap.data.api.HomeApi
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.data.repository.CryptoDetailsRepository
import br.eti.rafaelcouto.cryptocap.data.repository.HomeRepository
import br.eti.rafaelcouto.cryptocap.data.repository.abs.CryptoDetailsRepositoryAbs
import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs
import br.eti.rafaelcouto.cryptocap.data.source.HomePagingSource
import br.eti.rafaelcouto.cryptocap.domain.mapper.CryptoCompareMapper
import br.eti.rafaelcouto.cryptocap.domain.mapper.CryptoDetailsMapper
import br.eti.rafaelcouto.cryptocap.domain.mapper.CryptoItemMapper
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoCompareMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoDetailsMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoItemMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.usecase.CryptoCompareUseCase
import br.eti.rafaelcouto.cryptocap.domain.usecase.CryptoDetailsUseCase
import br.eti.rafaelcouto.cryptocap.domain.usecase.HomeUseCase
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoCompareUseCaseAbs
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoDetailsUseCaseAbs
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import br.eti.rafaelcouto.cryptocap.viewmodel.CryptoCompareViewModel
import br.eti.rafaelcouto.cryptocap.viewmodel.CryptoDetailsViewModel
import br.eti.rafaelcouto.cryptocap.viewmodel.HomeViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Modules {

    const val HEADER_INTERCEPTOR = "headerInterceptor"
    const val LOGGING_INTERCEPTOR = "loggingInterceptor"

    private val network = module {
        single<Interceptor>(named(LOGGING_INTERCEPTOR)) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        single<Interceptor>(named(HEADER_INTERCEPTOR)) { HeaderInterceptor() }
        single<Converter.Factory> { MoshiConverterFactory.create() }
        single<CallAdapter.Factory> { ResultAdapterFactory.invoke() }

        single {
            OkHttpClient.Builder()
                .addInterceptor(get<Interceptor>(named(HEADER_INTERCEPTOR)))
                .addInterceptor(get<Interceptor>(named(LOGGING_INTERCEPTOR)))
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
        single {
            val retrofit: Retrofit = get()
            retrofit.create(HomeApi::class.java)
        }

        single {
            val retrofit: Retrofit = get()
            retrofit.create(CryptoDetailsApi::class.java)
        }
    }

    private val paging = module {
        single<PagingSource<Int, CryptoItem>> { HomePagingSource(api = get()) }

        single { PagingConfig(HomeRepository.DEFAULT_LIST_SIZE) }

        single { Pager(get()) { get<PagingSource<Int, CryptoItem>>() } }
    }

    private val repository = module {
        single<HomeRepositoryAbs> { HomeRepository(pager = get()) }
        single<CryptoDetailsRepositoryAbs> { CryptoDetailsRepository(api = get()) }
    }

    private val useCase = module {
        single<HomeUseCaseAbs> {
            HomeUseCase(
                repository = get(),
                cryptoItemMapper = get()
            )
        }

        single<CryptoDetailsUseCaseAbs> {
            CryptoDetailsUseCase(
                repository = get(),
                mapper = get()
            )
        }

        single<CryptoCompareUseCaseAbs> {
            CryptoCompareUseCase(
                repository = get(),
                mapper = get()
            )
        }
    }

    private val mapper = module {
        single<CryptoItemMapperAbs> { CryptoItemMapper() }
        single<CryptoDetailsMapperAbs> { CryptoDetailsMapper() }
        single<CryptoCompareMapperAbs> { CryptoCompareMapper() }
    }

    private val viewModel = module {
        viewModel { HomeViewModel(useCase = get()) }
        viewModel { CryptoDetailsViewModel(useCase = get()) }
        viewModel { CryptoCompareViewModel(useCase = get()) }
    }

    val all = listOf(network, api, paging, repository, useCase, mapper, viewModel)
}
