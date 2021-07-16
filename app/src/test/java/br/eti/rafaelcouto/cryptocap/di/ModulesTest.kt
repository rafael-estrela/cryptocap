package br.eti.rafaelcouto.cryptocap.di

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import br.eti.rafaelcouto.cryptocap.application.network.adapter.ResultAdapterFactory
import br.eti.rafaelcouto.cryptocap.application.network.interceptor.HeaderInterceptor
import br.eti.rafaelcouto.cryptocap.data.api.CryptoDetailsApi
import br.eti.rafaelcouto.cryptocap.data.api.HomeApi
import br.eti.rafaelcouto.cryptocap.data.local.FavoriteDatabase
import br.eti.rafaelcouto.cryptocap.data.local.dao.FavoriteDao
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
        val detailsApi: CryptoDetailsApi by inject()

        assertThat(homeApi).isNotNull()
        assertThat(detailsApi).isNotNull()
    }

    @Test
    fun databaseTest() {
        val database: FavoriteDatabase by inject()

        assertThat(database).isNotNull()
    }

    @Test
    fun daoTest() {
        val dao: FavoriteDao by inject()

        assertThat(dao).isNotNull()
    }

    @Test
    fun pagerModulesTest() {
        val homeSource: PagingSource<Int, CryptoItem> by inject()
        val pagingConfig: PagingConfig by inject()
        val pager: Pager<Int, CryptoItem> by inject()

        assertThat(homeSource).isInstanceOf(HomePagingSource::class.java)
        assertThat(pagingConfig).isNotNull()
        assertThat(pagingConfig.pageSize).isEqualTo(HomeRepository.DEFAULT_LIST_SIZE)
        assertThat(pager).isNotNull()
    }

    @Test
    fun repositoryModulesTest() {
        val homeRepository: HomeRepositoryAbs by inject()
        val cryptoDetailsRepository: CryptoDetailsRepositoryAbs by inject()

        assertThat(homeRepository).isInstanceOf(HomeRepository::class.java)
        assertThat(cryptoDetailsRepository).isInstanceOf(CryptoDetailsRepository::class.java)
    }

    @Test
    fun useCaseModulesTest() {
        val homeUseCase: HomeUseCaseAbs by inject()
        val detailsUseCase: CryptoDetailsUseCaseAbs by inject()
        val compareUseCase: CryptoCompareUseCaseAbs by inject()

        assertThat(homeUseCase).isInstanceOf(HomeUseCase::class.java)
        assertThat(detailsUseCase).isInstanceOf(CryptoDetailsUseCase::class.java)
        assertThat(compareUseCase).isInstanceOf(CryptoCompareUseCase::class.java)
    }

    @Test
    fun mapperModulesTest() {
        val cryptoItemMapper: CryptoItemMapperAbs by inject()
        val cryptoDetailsMapper: CryptoDetailsMapperAbs by inject()
        val cryptoCompareMapper: CryptoCompareMapperAbs by inject()

        assertThat(cryptoItemMapper).isInstanceOf(CryptoItemMapper::class.java)
        assertThat(cryptoDetailsMapper).isInstanceOf(CryptoDetailsMapper::class.java)
        assertThat(cryptoCompareMapper).isInstanceOf(CryptoCompareMapper::class.java)
    }

    @Test
    fun viewModelModulesTest() {
        val homeViewModel: HomeViewModel by inject()
        val cryptoDetailsViewModel: CryptoDetailsViewModel by inject()
        val cryptoCompareViewModel: CryptoCompareViewModel by inject()

        assertThat(homeViewModel).isNotNull()
        assertThat(cryptoDetailsViewModel).isNotNull()
        assertThat(cryptoCompareViewModel).isNotNull()
    }
}
