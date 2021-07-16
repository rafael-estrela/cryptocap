package br.eti.rafaelcouto.cryptocap.domain.usecase

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.data.repository.abs.CryptoDetailsRepositoryAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoDetailsMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoDetailsUseCaseAbs
import br.eti.rafaelcouto.cryptocap.testhelper.factory.DetailsFactory
import br.eti.rafaelcouto.cryptocap.testhelper.factory.FavoriteFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CryptoDetailsUseCaseTest {

    @RelaxedMockK private lateinit var mockRepository: CryptoDetailsRepositoryAbs
    @MockK private lateinit var mockMapper: CryptoDetailsMapperAbs

    private lateinit var sut: CryptoDetailsUseCaseAbs

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = CryptoDetailsUseCase(repository = mockRepository, mapper = mockMapper)
    }

    @Test
    fun fetchDetailsSuccessTest() = runBlocking {
        val detailsInput = Result.success(DetailsFactory.itemDetails)
        val quotesInput = Result.success(DetailsFactory.itemQuotes)

        coEvery { mockRepository.fetchDetails(any()) }.returns(flowOf(detailsInput))
        coEvery { mockRepository.fetchQuotes(any()) }.returns(flowOf(quotesInput))

        val expected = DetailsFactory.itemDetailsUi

        every { mockMapper.map(any(), any()) }.returns(Result.success(expected))

        sut.fetchDetails(1).collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
            assertThat(actual.error).isNull()
            assertThat(actual.data).isEqualTo(expected)
        }

        coVerify { mockRepository.fetchDetails(1) }
        coVerify { mockRepository.fetchQuotes(1) }
        verify { mockMapper.map(detailsInput, quotesInput) }
    }

    @Test
    fun fetchErrorTest() = runBlocking {
        val detailsInput = Result.error<CryptoDetails>("details error")
        val quotesInput = Result.error<QuoteDetails>("quote error")

        coEvery { mockRepository.fetchDetails(any()) }.returns(flowOf(detailsInput))
        coEvery { mockRepository.fetchQuotes(any()) }.returns(flowOf(quotesInput))

        val expected = "mapper error"

        every { mockMapper.map(any(), any()) }.returns(Result.error(expected))

        sut.fetchDetails(1).collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.ERROR)
            assertThat(actual.data).isNull()
            assertThat(actual.error).isEqualTo(expected)
        }

        coVerify { mockRepository.fetchDetails(1) }
        coVerify { mockRepository.fetchQuotes(1) }
        verify { mockMapper.map(detailsInput, quotesInput) }
    }

    @Test
    fun isFavoriteTest() = runBlocking {
        coEvery { mockRepository.fetchFavorite(any()) }.returns(FavoriteFactory.default)

        val actual = sut.isFavorite(1)

        coVerify { mockRepository.fetchFavorite(1) }

        assertThat(actual).isTrue()
    }

    @Test
    fun isNotFavoriteTest() = runBlocking {
        coEvery { mockRepository.fetchFavorite(any()) }.returns(null)

        val actual = sut.isFavorite(1)

        coVerify { mockRepository.fetchFavorite(1) }

        assertThat(actual).isFalse()
    }

    @Test
    fun saveToFavoritesTest() = runBlocking {
        val input = FavoriteFactory.fromId(1)

        every { mockMapper.map(any()) }.returns(input)

        sut.saveToFavorites(1)

        verify { mockMapper.map(1) }
        coVerify { mockRepository.addToFavorites(input) }
    }

    @Test
    fun removeFromFavoritesTest() = runBlocking {
        val input = FavoriteFactory.fromId(1)

        every { mockMapper.map(any()) }.returns(input)

        sut.removeFromFavorites(1)

        verify { mockMapper.map(1) }
        coVerify { mockRepository.removeFromFavorites(input) }
    }
}
