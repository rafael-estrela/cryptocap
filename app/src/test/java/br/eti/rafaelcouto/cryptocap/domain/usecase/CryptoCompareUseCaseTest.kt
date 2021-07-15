package br.eti.rafaelcouto.cryptocap.domain.usecase

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.data.repository.abs.CryptoDetailsRepositoryAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoCompareMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoCompareUseCaseAbs
import br.eti.rafaelcouto.cryptocap.testhelper.factory.CompareFactory
import br.eti.rafaelcouto.cryptocap.testhelper.factory.DetailsFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CryptoCompareUseCaseTest {

    @MockK private lateinit var mockRepository: CryptoDetailsRepositoryAbs
    @MockK private lateinit var mockMapper: CryptoCompareMapperAbs

    private lateinit var sut: CryptoCompareUseCaseAbs

    private companion object {
        const val FROM_ID = 1L
        const val TO_ID = 2L
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = CryptoCompareUseCase(
            repository = mockRepository,
            mapper = mockMapper
        )
    }

    @Test
    fun fetchSuccessTest() = runBlocking {
        val detailsFromInput = Result.success(DetailsFactory.itemDetails)
        val quotesFromInput = Result.success(DetailsFactory.itemQuotes)

        every { mockRepository.fetchDetails(FROM_ID) }.returns(flowOf(detailsFromInput))
        every { mockRepository.fetchQuotes(FROM_ID) }.returns(flowOf(quotesFromInput))

        val detailsToInput = Result.success(DetailsFactory.itemDetails)
        val quotesToInput = Result.success(DetailsFactory.itemQuotes)

        every { mockRepository.fetchDetails(TO_ID) }.returns(flowOf(detailsToInput))
        every { mockRepository.fetchQuotes(TO_ID) }.returns(flowOf(quotesToInput))

        val fromInputData = CompareFactory.element
        val toInputData = CompareFactory.element

        val fromInput = Result.success(fromInputData)
        val toInput = Result.success(toInputData)

        every { mockMapper.mapElement(detailsFromInput, quotesFromInput) }.returns(fromInput)
        every { mockMapper.mapElement(detailsToInput, quotesToInput) }.returns(toInput)

        val expected = CompareFactory.fromElements(fromInputData, toInputData)
        every { mockMapper.map(any<Result<CryptoCompareUI.Element>>(), any()) }.returns(Result.success(expected))

        sut.fetchInfo(1, 2).collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
            assertThat(actual.error).isNull()
            assertThat(actual.data).isEqualTo(expected)
        }

        verify(exactly = 1) { mockMapper.map(fromInput, toInput) }
    }

    @Test
    fun fetchErrorTest() = runBlocking {
        val detailsFromInput = Result.error<CryptoDetails>("dummy error")
        val quotesFromInput = Result.error<QuoteDetails>("dummy error")

        every { mockRepository.fetchDetails(FROM_ID) }.returns(flowOf(detailsFromInput))
        every { mockRepository.fetchQuotes(FROM_ID) }.returns(flowOf(quotesFromInput))

        val detailsToInput = Result.error<CryptoDetails>("dummy error")
        val quotesToInput = Result.error<QuoteDetails>("dummy error")

        every { mockRepository.fetchDetails(TO_ID) }.returns(flowOf(detailsToInput))
        every { mockRepository.fetchQuotes(TO_ID) }.returns(flowOf(quotesToInput))

        val fromInput = Result.error<CryptoCompareUI.Element>("dummy error")
        val toInput = Result.error<CryptoCompareUI.Element>("dummy error")

        every { mockMapper.mapElement(detailsFromInput, quotesFromInput) }.returns(fromInput)
        every { mockMapper.mapElement(detailsToInput, quotesToInput) }.returns(toInput)

        every { mockMapper.map(any<Result<CryptoCompareUI.Element>>(), any()) }.returns(Result.error("dummy error"))

        sut.fetchInfo(1, 2).collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.ERROR)
            assertThat(actual.data).isNull()
            assertThat(actual.error).isEqualTo("dummy error")
        }

        verify(exactly = 1) { mockMapper.map(fromInput, toInput) }
    }

    @Test
    fun swapTest() {
        val elements = CompareFactory.biggerFrom
        val uiElement = CompareFactory.fromElements(elements.first, elements.second)

        every { mockMapper.map(any<CryptoCompareUI.Element>(), any()) }.returns(uiElement)

        sut.swap(uiElement)

        verify { mockMapper.map(uiElement.to, uiElement.from) }
    }
}
