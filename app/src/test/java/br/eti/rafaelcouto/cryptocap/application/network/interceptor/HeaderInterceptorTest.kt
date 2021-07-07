package br.eti.rafaelcouto.cryptocap.application.network.interceptor

import br.eti.rafaelcouto.cryptocap.ApiTest
import br.eti.rafaelcouto.cryptocap.BuildConfig
import br.eti.rafaelcouto.cryptocap.application.network.RequestConstants
import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HeaderInterceptorTest : ApiTest() {

    private lateinit var sut: HeaderInterceptor

    @Before
    fun setUp() {
        sut = HeaderInterceptor()
    }

    @Test
    fun includeHeaderTest() {
        mockWebServer.enqueue(MockResponse())

        val client = OkHttpClient.Builder().addInterceptor(sut).build()
        client.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        val actual = mockWebServer.takeRequest()

        assertThat(actual.getHeader(RequestConstants.API_KEY_HEADER)).isEqualTo(BuildConfig.API_KEY)
    }
}
