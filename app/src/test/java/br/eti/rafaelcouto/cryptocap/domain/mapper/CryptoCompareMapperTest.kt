package br.eti.rafaelcouto.cryptocap.domain.mapper

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoCompareMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI
import br.eti.rafaelcouto.cryptocap.testhelper.factory.CompareFactory
import br.eti.rafaelcouto.cryptocap.testhelper.factory.DetailsFactory
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CryptoCompareMapperTest {

    private lateinit var sut: CryptoCompareMapperAbs

    @Before
    fun setUp() {
        sut = CryptoCompareMapper()
    }

    @Test
    fun mapElementSuccessTest() {
        val details = Result.success(DetailsFactory.itemDetails)
        val quotes = Result.success(DetailsFactory.itemQuotes)

        val result = sut.mapElement(details, quotes)

        assertThat(result.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(result.error).isNull()
        assertThat(result.data).isNotNull()

        result.data?.let { actual ->
            assertThat(actual.symbol).isEqualTo("BTC")
            assertThat(actual.logoUrl).isEqualTo("https://s2.coinmarketcap.com/static/img/coins/64x64/1.png")
            assertThat(actual.usdValue).isEqualTo(1412.09)
        }
    }

    @Test
    fun mapElementDetailsErrorTest() {
        val details = Result.error<CryptoDetails>("details error")
        val quotes = Result.success(DetailsFactory.itemQuotes)

        val result = sut.mapElement(details, quotes)

        assertThat(result.status).isEqualTo(Result.Status.ERROR)
        assertThat(result.data).isNull()
        assertThat(result.error).isEqualTo("details error")
    }

    @Test
    fun mapElementQuotesErrorTest() {
        val details = Result.success(DetailsFactory.itemDetails)
        val quotes = Result.error<QuoteDetails>("quotes error")

        val result = sut.mapElement(details, quotes)

        assertThat(result.status).isEqualTo(Result.Status.ERROR)
        assertThat(result.data).isNull()
        assertThat(result.error).isEqualTo("quotes error")
    }

    @Test
    fun mapElementAllErrorsTest() {
        val details = Result.error<CryptoDetails>("details error")
        val quotes = Result.error<QuoteDetails>("quotes error")

        val result = sut.mapElement(details, quotes)

        assertThat(result.status).isEqualTo(Result.Status.ERROR)
        assertThat(result.data).isNull()
        assertThat(result.error).isEqualTo("details error")
    }

    @Test
    fun mapSuccessHighRatioTest() {
        val input = CompareFactory.biggerFrom
        val fromInput = Result.success(input.first)
        val toInput = Result.success(input.second)

        val result = sut.map(fromInput, toInput)

        assertThat(result.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(result.error).isNull()
        assertThat(result.data).isNotNull()

        result.data?.let { actual ->
            assertThat(actual.from).isEqualTo(input.first)
            assertThat(actual.to).isEqualTo(input.second)
            assertThat(actual.fromToRatio).isEqualTo(2.0)
        }
    }

    @Test
    fun mapSuccessLowRatioTest() {
        val input = CompareFactory.biggerTo
        val fromInput = Result.success(input.first)
        val toInput = Result.success(input.second)

        val result = sut.map(fromInput, toInput)

        assertThat(result.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(result.error).isNull()
        assertThat(result.data).isNotNull()

        result.data?.let { actual ->
            assertThat(actual.from).isEqualTo(input.first)
            assertThat(actual.to).isEqualTo(input.second)
            assertThat(actual.fromToRatio).isEqualTo(0.5)
        }
    }

    @Test
    fun mapSuccessEqualRatioTest() {
        val input = CompareFactory.equalFromTo
        val fromInput = Result.success(input.first)
        val toInput = Result.success(input.second)

        val result = sut.map(fromInput, toInput)

        assertThat(result.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(result.error).isNull()
        assertThat(result.data).isNotNull()

        result.data?.let { actual ->
            assertThat(actual.from).isEqualTo(input.first)
            assertThat(actual.to).isEqualTo(input.second)
            assertThat(actual.fromToRatio).isEqualTo(1.0)
        }
    }

    @Test
    fun mapFromErrorTest() {
        val fromInput = Result.error<CryptoCompareUI.Element>("from error")
        val toInput = Result.success(CompareFactory.element)

        val actual = sut.map(fromInput, toInput)

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.data).isNull()
        assertThat(actual.error).isEqualTo("from error")
    }

    @Test
    fun mapToErrorTest() {
        val fromInput = Result.success(CompareFactory.element)
        val toInput = Result.error<CryptoCompareUI.Element>("to error")

        val actual = sut.map(fromInput, toInput)

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.data).isNull()
        assertThat(actual.error).isEqualTo("to error")
    }

    @Test
    fun mapAllErrorsTest() {
        val fromInput = Result.error<CryptoCompareUI.Element>("from error")
        val toInput = Result.error<CryptoCompareUI.Element>("to error")

        val actual = sut.map(fromInput, toInput)

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.data).isNull()
        assertThat(actual.error).isEqualTo("from error")
    }

    @Test
    fun mapTest() {
        val fromInput = CompareFactory.element
        val toInput = CompareFactory.element

        val actual = sut.map(fromInput, toInput)

        assertThat(actual.from).isEqualTo(fromInput)
        assertThat(actual.to).isEqualTo(toInput)
        assertThat(actual.fromToRatio).isEqualTo(1.0)
    }
}
