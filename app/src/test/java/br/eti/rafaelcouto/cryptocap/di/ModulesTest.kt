package br.eti.rafaelcouto.cryptocap.di

import android.content.Context
import br.eti.rafaelcouto.cryptocap.application.network.adapter.ResultAdapterFactory
import br.eti.rafaelcouto.cryptocap.application.network.interceptor.HeaderInterceptor
import br.eti.rafaelcouto.cryptocap.data.api.HomeApi
import br.eti.rafaelcouto.cryptocap.data.repository.HomeRepository
import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.CryptoItemMapper
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoItemMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.usecase.HomeUseCase
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import br.eti.rafaelcouto.cryptocap.viewmodel.HomeViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.mockkClass
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.inject
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(JUnit4::class)
class ModulesTest : KoinTest {

    @Before
    fun setUp() {
        startKoin {
            androidContext(mockkClass(Context::class))
            modules(Modules.all)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun networkModulesTest() {
        val headerInterceptor: Interceptor by inject(named(Modules.HEADER_INTERCEPTOR))
        val loggingInterceptor: Interceptor by inject(named(Modules.LOGGING_INTERCEPTOR))
        val converterFactory: Converter.Factory by inject()
        val callAdapterFactory: CallAdapter.Factory by inject()
        val client: OkHttpClient by inject()
        val retrofit: Retrofit by inject()

        assertThat(headerInterceptor).isInstanceOf(HeaderInterceptor::class.java)
        assertThat(loggingInterceptor).isInstanceOf(HttpLoggingInterceptor::class.java)
        assertThat(converterFactory).isInstanceOf(MoshiConverterFactory::class.java)
        assertThat(callAdapterFactory).isInstanceOf(ResultAdapterFactory::class.java)
        assertThat(client).isNotNull()
        assertThat(retrofit).isNotNull()
    }

    @Test
    fun apiModulesTest() {
        val homeApi: HomeApi by inject()

        assertThat(homeApi).isNotNull()
    }

    @Test
    fun repositoryModulesTest() {
        val homeRepository: HomeRepositoryAbs by inject()

        assertThat(homeRepository).isInstanceOf(HomeRepository::class.java)
    }

    @Test
    fun useCaseModulesTest() {
        val homeUseCase: HomeUseCaseAbs by inject()

        assertThat(homeUseCase).isInstanceOf(HomeUseCase::class.java)
    }

    @Test
    fun mapperModulesTest() {
        val cryptoItemMapper: CryptoItemMapperAbs by inject()

        assertThat(cryptoItemMapper).isInstanceOf(CryptoItemMapper::class.java)
    }

    @Test
    fun viewModelModulesTest() {
        val homeViewModel: HomeViewModel by inject()

        assertThat(homeViewModel).isNotNull()
    }
}