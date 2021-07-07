package br.eti.rafaelcouto.cryptocap.application.network

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RequestConstantsTest {

    @Test
    fun defaultTimeoutTest() {
        assertThat(RequestConstants.DEFAULT_TIMEOUT).isEqualTo(15L)
    }

    @Test
    fun apiKeyHeaderTest() {
        assertThat(RequestConstants.API_KEY_HEADER).isEqualTo("X-CMC_PRO_API_KEY")
    }
}
