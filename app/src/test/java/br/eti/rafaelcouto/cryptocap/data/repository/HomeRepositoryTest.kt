package br.eti.rafaelcouto.cryptocap.data.repository

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.api.HomeApi
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs
import br.eti.rafaelcouto.cryptocap.factory.HomeFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeRepositoryTest {

    @MockK private lateinit var mockApi: HomeApi

    private lateinit var sut: HomeRepositoryAbs

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = HomeRepository(api = mockApi)
    }

    @Test
    fun fetchAllSuccessTest() = runBlocking {
        val expected = Result.success(HomeFactory.cryptoList)

        coEvery { mockApi.fetchAll() }.returns(expected)

        sut.fetchAll().collect { actual ->
            assertThat(actual.status).isEqualTo(expected.status)
            assertThat(actual.error).isEqualTo(expected.error)
            assertThat(actual.data).isEqualTo(expected.data)
        }
    }

    @Test
    fun fetchAllErrorTest() = runBlocking {
        val expected = Result.error<List<CryptoItem>>("dummy error")

        coEvery { mockApi.fetchAll() }.returns(expected)

        sut.fetchAll().collect { actual ->
            assertThat(actual.status).isEqualTo(expected.status)
            assertThat(actual.error).isEqualTo(expected.error)
            assertThat(actual.data).isEqualTo(expected.data)
        }
    }
}
