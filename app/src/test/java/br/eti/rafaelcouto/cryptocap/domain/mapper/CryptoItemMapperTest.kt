package br.eti.rafaelcouto.cryptocap.domain.mapper

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoItemMapperAbs
import br.eti.rafaelcouto.cryptocap.factory.HomeFactory
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CryptoItemMapperTest {

    private lateinit var sut: CryptoItemMapperAbs

    @Before
    fun setUp() {
        sut = CryptoItemMapper()
    }

    @Test
    fun mapErrorDataTest() {
        val input = Result.error<List<CryptoItem>>("dummy error")
        val actual = sut.map(input)

        assertThat(actual.data).isNull()
        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.error).isEqualTo("dummy error")
    }

    @Test
    fun mapSuccessDataTest() {
        val input = Result.success(HomeFactory.cryptoList)
        val actual = sut.map(input)

        assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(actual.error).isNull()

        assertThat(actual.data).isNotNull()
        assertThat(actual.data).isNotEmpty()

        actual.data?.forEachIndexed { index, item ->
            assertThat(item.id).isEqualTo(input.data?.get(index)?.id)
            assertThat(item.name).isEqualTo("Dummy Coin")
            assertThat(item.symbol).isEqualTo("DMC")
            assertThat(item.dollarPrice).isEqualTo("US$ 1,412.09")
            assertThat(item.recentVariation).isEqualTo("51.33%")
        }
    }
}
