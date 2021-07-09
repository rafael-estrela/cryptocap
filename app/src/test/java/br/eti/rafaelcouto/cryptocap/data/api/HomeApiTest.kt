package br.eti.rafaelcouto.cryptocap.data.api

import br.eti.rafaelcouto.cryptocap.ApiTest
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeApiTest : ApiTest() {

    private lateinit var sut: HomeApi

    @Before
    fun setUp() {
        sut = buildApi()
    }

    @Test
    fun getListSuccessTest() = runBlocking {
        enqueueResponseFromFile("list_success.json")
        val actual = sut.fetchAll()

        assertThat(actual.data).isNotNull()
        assertThat(actual.data).isNotEmpty()
        assertThat(actual.data).hasSize(10)

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

            assertThat(data[5].id).isEqualTo(52)
            assertThat(data[5].name).isEqualTo("XRP")
            assertThat(data[5].symbol).isEqualTo("XRP")
            assertThat(data[5].quote.usdQuote.price).isEqualTo(0.66274047655856)
            assertThat(data[5].quote.usdQuote.dayVariation).isEqualTo(-0.18425307)

            assertThat(data[6].id).isEqualTo(74)
            assertThat(data[6].name).isEqualTo("Dogecoin")
            assertThat(data[6].symbol).isEqualTo("DOGE")
            assertThat(data[6].quote.usdQuote.price).isEqualTo(0.23224748540349)
            assertThat(data[6].quote.usdQuote.dayVariation).isEqualTo(-1.10136679)

            assertThat(data[7].id).isEqualTo(3408)
            assertThat(data[7].name).isEqualTo("USD Coin")
            assertThat(data[7].symbol).isEqualTo("USDC")
            assertThat(data[7].quote.usdQuote.price).isEqualTo(1.00000883985712)
            assertThat(data[7].quote.usdQuote.dayVariation).isEqualTo(-0.04651697)

            assertThat(data[8].id).isEqualTo(6636)
            assertThat(data[8].name).isEqualTo("Polkadot")
            assertThat(data[8].symbol).isEqualTo("DOT")
            assertThat(data[8].quote.usdQuote.price).isEqualTo(15.98897287265222)
            assertThat(data[8].quote.usdQuote.dayVariation).isEqualTo(4.48603884)

            assertThat(data[9].id).isEqualTo(7083)
            assertThat(data[9].name).isEqualTo("Uniswap")
            assertThat(data[9].symbol).isEqualTo("UNI")
            assertThat(data[9].quote.usdQuote.price).isEqualTo(22.8623533209434)
            assertThat(data[9].quote.usdQuote.dayVariation).isEqualTo(15.47811344)
        }

        assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(actual.error).isNull()
    }

    @Test
    fun getListErrorTest() = runBlocking {
        enqueueResponseFromFile("list_invalid_api_key.json", 401)
        val actual = sut.fetchAll()
        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.error).isEqualTo("This API Key is invalid.")
        assertThat(actual.data).isNull()
    }
}
