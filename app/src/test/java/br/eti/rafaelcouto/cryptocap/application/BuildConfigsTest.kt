package br.eti.rafaelcouto.cryptocap.application

import br.eti.rafaelcouto.cryptocap.BuildConfig
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BuildConfigsTest {

    @Test
    fun checkApiKeyTest() {
        val actual = BuildConfig.API_KEY
        val expected = "2ec8b9db-3efc-483e-a736-d5365813a50e"

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun checkBaseUrlTest() {
        val actual = BuildConfig.BASE_URL
        val expected = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/"

        assertThat(actual).isEqualTo(expected)
    }
}
