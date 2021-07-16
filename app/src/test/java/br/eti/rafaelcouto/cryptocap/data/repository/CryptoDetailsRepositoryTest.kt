package br.eti.rafaelcouto.cryptocap.data.repository

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.api.CryptoDetailsApi
import br.eti.rafaelcouto.cryptocap.data.local.dao.FavoriteDao
import br.eti.rafaelcouto.cryptocap.data.repository.abs.CryptoDetailsRepositoryAbs
import br.eti.rafaelcouto.cryptocap.testhelper.factory.DetailsFactory
import br.eti.rafaelcouto.cryptocap.testhelper.factory.FavoriteFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CryptoDetailsRepositoryTest {

    @MockK private lateinit var mockApi: CryptoDetailsApi
    @RelaxedMockK private lateinit var mockDao: FavoriteDao

    private lateinit var sut: CryptoDetailsRepositoryAbs

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = CryptoDetailsRepository(api = mockApi, dao = mockDao)
    }

    @Test
    fun fetchDetailsSuccessTest() = runBlocking {
        val expected = DetailsFactory.itemDetails
        coEvery { mockApi.fetchInfo(any()) }.returns(Result.success(expected))

        sut.fetchDetails(1).collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
            assertThat(actual.error).isNull()
            assertThat(actual.data).isEqualTo(expected)
        }

        coVerify { mockApi.fetchInfo(1) }
        coVerify(exactly = 0) { mockApi.fetchLatestQuotes(any()) }
    }

    @Test
    fun fetchDetailsFailureTest() = runBlocking {
        val expected = "dummy error"
        coEvery { mockApi.fetchInfo(any()) }.returns(Result.error(expected))

        sut.fetchDetails(1).collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.ERROR)
            assertThat(actual.data).isNull()
            assertThat(actual.error).isEqualTo(expected)
        }

        coVerify(exactly = 1) { mockApi.fetchInfo(1) }
        coVerify(exactly = 0) { mockApi.fetchLatestQuotes(any()) }
    }

    @Test
    fun fetchQuotesSuccessTest() = runBlocking {
        val expected = DetailsFactory.itemQuotes
        coEvery { mockApi.fetchLatestQuotes(any()) }.returns(Result.success(expected))

        sut.fetchQuotes(1).collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
            assertThat(actual.error).isNull()
            assertThat(actual.data).isEqualTo(expected)
        }

        coVerify(exactly = 1) { mockApi.fetchLatestQuotes(1) }
        coVerify(exactly = 0) { mockApi.fetchInfo(any()) }
    }

    @Test
    fun fetchQuotesFailureTest() = runBlocking {
        val expected = "dummy error"
        coEvery { mockApi.fetchLatestQuotes(any()) }.returns(Result.error(expected))

        sut.fetchQuotes(1).collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.ERROR)
            assertThat(actual.data).isNull()
            assertThat(actual.error).isEqualTo(expected)
        }

        coVerify(exactly = 1) { mockApi.fetchLatestQuotes(1) }
        coVerify(exactly = 0) { mockApi.fetchInfo(any()) }
    }

    @Test
    fun fetchFavoritesTest() = runBlocking {
        val expected = FavoriteFactory.default
        coEvery { mockDao.findById(any()) }.returns(expected)

        val actual = sut.fetchFavorite(1)

        coVerify { mockDao.findById(1) }
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun addToFavoritesTest() = runBlocking {
        val input = FavoriteFactory.default
        sut.addToFavorites(input)

        coVerify { mockDao.insert(input) }
    }

    @Test
    fun removeFromFavoritesTest() = runBlocking {
        val input = FavoriteFactory.default
        sut.removeFromFavorites(input)

        coVerify { mockDao.delete(input) }
    }
}
