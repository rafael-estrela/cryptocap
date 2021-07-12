package br.eti.rafaelcouto.cryptocap.data.api

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.repository.HomeRepository
import br.eti.rafaelcouto.cryptocap.data.source.HomePagingSource
import br.eti.rafaelcouto.cryptocap.testhelper.base.ApiTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class HomeApiTest : ApiTest() {

    private lateinit var sut: HomeApi

    @Before
    fun setUp() {
        sut = buildApi()
    }

    @Test
    fun getListRquestTest() = runBlocking {
        enqueueResponseFromFile("list_success.json")
        sut.fetchAll(HomePagingSource.INITIAL_START, HomeRepository.DEFAULT_LIST_SIZE)
        val request = mockWebServer.takeRequest()

        request.path.run {
            assertThat(this).startsWith("/listings/latest?")
            assertThat(this).contains("start=1")
            assertThat(this).contains("limit=15")
        }

        assertThat(request.method).ignoringCase().isEqualTo("GET")
    }

    @Test
    fun getListSuccessTest() = runBlocking {
        enqueueResponseFromFile("list_success.json")
        val actual = sut.fetchAll(HomePagingSource.INITIAL_START, HomeRepository.DEFAULT_LIST_SIZE)

        assertThat(actual.data).isNotNull()
        assertThat(actual.data).isNotEmpty()
        assertThat(actual.data).hasSize(5)

        actual.data?.let { data ->
            assertThat(data[0].id).isEqualTo(1)
            assertThat(data[0].name).isEqualTo("Bitcoin")
            assertThat(data[0].symbol).isEqualTo("BTC")
            assertThat(data[0].quote.usdQuote.price).isEqualTo(34003.04515703448)
            assertThat(data[0].quote.usdQuote.dayVariation).isEqualTo(-0.07257688)

            assertThat(data[1].id).isEqualTo(1027)
            assertThat(data[1].name).isEqualTo("Ethereum")
            assertThat(data[1].symbol).isEqualTo("ETH")
            assertThat(data[1].quote.usdQuote.price).isEqualTo(2325.111622170845)
            assertThat(data[1].quote.usdQuote.dayVariation).isEqualTo(4.51764208)

            assertThat(data[2].id).isEqualTo(825)
            assertThat(data[2].name).isEqualTo("Tether")
            assertThat(data[2].symbol).isEqualTo("USDT")
            assertThat(data[2].quote.usdQuote.price).isEqualTo(1.00000453858029)
            assertThat(data[2].quote.usdQuote.dayVariation).isEqualTo(-0.0791239)

            assertThat(data[3].id).isEqualTo(1839)
            assertThat(data[3].name).isEqualTo("Binance Coin")
            assertThat(data[3].symbol).isEqualTo("BNB")
            assertThat(data[3].quote.usdQuote.price).isEqualTo(319.30427146237577)
            assertThat(data[3].quote.usdQuote.dayVariation).isEqualTo(5.59973651)

            assertThat(data[4].id).isEqualTo(2010)
            assertThat(data[4].name).isEqualTo("Cardano")
            assertThat(data[4].symbol).isEqualTo("ADA")
            assertThat(data[4].quote.usdQuote.price).isEqualTo(1.41064547096802)
            assertThat(data[4].quote.usdQuote.dayVariation).isEqualTo(-0.2703349)
        }

        assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(actual.error).isNull()
    }

    @Test
    fun getListErrorTest() = runBlocking {
        enqueueResponseFromFile("list_invalid_api_key.json", HttpURLConnection.HTTP_UNAUTHORIZED)
        val actual = sut.fetchAll(HomePagingSource.INITIAL_START, HomeRepository.DEFAULT_LIST_SIZE)
        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.error).isEqualTo("This API Key is invalid.")
        assertThat(actual.data).isNull()
    }
}
