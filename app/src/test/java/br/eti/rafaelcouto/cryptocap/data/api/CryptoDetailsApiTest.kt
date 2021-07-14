package br.eti.rafaelcouto.cryptocap.data.api

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.testhelper.base.ApiTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class CryptoDetailsApiTest : ApiTest() {

    private lateinit var sut: CryptoDetailsApi

    @Before
    fun setUp() {
        sut = buildApi()
    }

    @Test
    fun loadCryptoDetailsRequestTest() = runBlocking {
        enqueueResponseFromFile("details_success.json")
        sut.fetchInfo(1)
        val request = mockWebServer.takeRequest()

        request.path.let {
            assertThat(it).startsWith("/info?")
            assertThat(it).contains("id=1")
            assertThat(it).doesNotContain("&")
        }

        assertThat(request.method).ignoringCase().isEqualTo("GET")
    }

    @Test
    fun loadCryptoDetailsSuccessTest() = runBlocking {
        enqueueResponseFromFile("details_success.json")

        val result = sut.fetchInfo(1)

        assertThat(result.data).isNotNull()

        result.data?.let { actual ->
            val description = "Bitcoin (BTC) is a cryptocurrency . Users are able to generate BTC " +
                    "through the process of mining. Bitcoin has a current supply of 18,754,631. The " +
                    "last known price of Bitcoin is 32,906.01273372 USD and is down -3.03 over the " +
                    "last 24 hours. It is currently trading on 9018 active market(s) with " +
                    "\$24,692,225,809.62 traded over the last 24 hours. More information can be found " +
                    "at https://bitcoin.org/."

            assertThat(actual.id).isEqualTo(1)
            assertThat(actual.name).isEqualTo("Bitcoin")
            assertThat(actual.symbol).isEqualTo("BTC")
            assertThat(actual.description).isEqualTo(description)
            assertThat(actual.logo).isEqualTo("https://s2.coinmarketcap.com/static/img/coins/64x64/1.png")
        }

        assertThat(result.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(result.error).isNull()
    }

    @Test
    fun loadCryptoDetailsErrorTest() = runBlocking {
        enqueueResponseFromFile("invalid_api_key.json", HttpURLConnection.HTTP_UNAUTHORIZED)

        val actual = sut.fetchInfo(1)

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.error).isEqualTo("This API Key is invalid.")
        assertThat(actual.data).isNull()
    }

    @Test
    fun loadCryptoQuoteDetailsTest() = runBlocking {
        enqueueResponseFromFile("quotes_success.json")
        sut.fetchLatestQuotes(1)
        val request = mockWebServer.takeRequest()

        request.path.let {
            assertThat(it).startsWith("/quotes/latest?")
            assertThat(it).contains("id=1")
            assertThat(it).doesNotContain("&")
        }

        assertThat(request.method).ignoringCase().isEqualTo("GET")
    }

    @Test
    fun loadCryptoQuoteSuccessTest() = runBlocking {
        enqueueResponseFromFile("quotes_success.json")

        val result = sut.fetchLatestQuotes(1)

        assertThat(result.data).isNotNull()

        result.data?.let {
            val actual = it.quote.usdQuote
            assertThat(actual.price).isEqualTo(32499.96448104055)
            assertThat(actual.dayVariation).isEqualTo(-2.96493265)
            assertThat(actual.weekVariation).isEqualTo(-5.02038292)
            assertThat(actual.monthVariation).isEqualTo(-9.45641804)
        }

        assertThat(result.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(result.error).isNull()
    }

    @Test
    fun loadCryptoQuoteErrorTest() = runBlocking {
        enqueueResponseFromFile("invalid_api_key.json", HttpURLConnection.HTTP_UNAUTHORIZED)

        val actual = sut.fetchLatestQuotes(1)

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.error).isEqualTo("This API Key is invalid.")
        assertThat(actual.data).isNull()
    }
}
