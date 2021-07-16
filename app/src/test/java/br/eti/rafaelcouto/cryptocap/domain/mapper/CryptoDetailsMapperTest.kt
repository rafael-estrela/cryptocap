package br.eti.rafaelcouto.cryptocap.domain.mapper

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.Favorite
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoDetailsMapperAbs
import br.eti.rafaelcouto.cryptocap.testhelper.factory.DetailsFactory
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CryptoDetailsMapperTest {

    private lateinit var sut: CryptoDetailsMapperAbs

    @Before
    fun setUp() {
        sut = CryptoDetailsMapper()
    }

    @Test
    fun mapAllSuccessTest() {
        val detailsInput = Result.success(DetailsFactory.itemDetails)
        val quotesInput = Result.success(DetailsFactory.itemQuotes)

        val result = sut.map(detailsInput, quotesInput)

        assertThat(result.data).isNotNull()

        result.data?.let { actual ->
            val description = "Bitcoin (BTC) is a cryptocurrency . Users are able to generate BTC " +
                "through the process of mining. Bitcoin has a current supply of 18,754,631. The " +
                "last known price of Bitcoin is 32,906.01273372 USD and is down -3.03 over the " +
                "last 24 hours. It is currently trading on 9018 active market(s) with " +
                "\$24,692,225,809.62 traded over the last 24 hours. More information can be found " +
                "at https://bitcoin.org/."

            assertThat(actual.id).isEqualTo(detailsInput.data?.id)
            assertThat(actual.name).isEqualTo("Bitcoin (BTC)")
            assertThat(actual.description).isEqualTo(description)
            assertThat(actual.logoUrl).isEqualTo("https://s2.coinmarketcap.com/static/img/coins/64x64/1.png")
            assertThat(actual.dollarPrice).isEqualTo("1 BTC = US$ 1,412.09")
            assertThat(actual.dayVariation).isEqualTo("51.33%")
            assertThat(actual.weekVariation).isEqualTo("0.378978%")
            assertThat(actual.monthVariation).isEqualTo("-12.7%")
        }

        assertThat(result.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(result.error).isNull()
    }

    @Test
    fun mapDetailsErrorTest() {
        val expected = "details input error"

        val detailsInput = Result.error<CryptoDetails>(expected)
        val quotesInput = Result.success(DetailsFactory.itemQuotes)

        val actual = sut.map(detailsInput, quotesInput)

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.data).isNull()
        assertThat(actual.error).isEqualTo(expected)
    }

    @Test
    fun mapQuotesErrorTest() {
        val expected = "quotes input error"

        val detailsInput = Result.success(DetailsFactory.itemDetails)
        val quotesInput = Result.error<QuoteDetails>(expected)

        val actual = sut.map(detailsInput, quotesInput)

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.data).isNull()
        assertThat(actual.error).isEqualTo(expected)
    }

    @Test
    fun mapAllErrorTest() {
        val detailsInput = Result.error<CryptoDetails>("details input error")
        val quotesInput = Result.error<QuoteDetails>("quotes input error")

        val actual = sut.map(detailsInput, quotesInput)

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.data).isNull()
        assertThat(actual.error).isEqualTo("details input error")
    }

    @Test
    fun mapFavoriteTest() {
        val actual = sut.map(1)

        assertThat(actual).isInstanceOf(Favorite::class.java)
        assertThat(actual.id).isEqualTo(1)
    }
}
