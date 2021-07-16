package br.eti.rafaelcouto.cryptocap.application.network.call

import br.eti.rafaelcouto.cryptocap.application.network.RequestConstants
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Callback

@RunWith(JUnit4::class)
class CryptoCallTest {

    private lateinit var sut: MockCall

    @Before
    fun setUp() {
        sut = MockCall()
    }

    @Test
    fun mapErrorFromExceptionSuccessTest() {
        val expected = "dummy exception"
        val actual = sut.fromException(Exception("dummy exception"))

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun mapErrorFromExceptionFallbackTest() {
        val expected = RequestConstants.DEFAULT_ERROR
        val actual = sut.fromException(Exception())

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun mapErrorFromJsonSuccessTest() {
        val json = "{" +
            "\"status\":{" +
            "\"timestamp\":\"2021-07-06T19:10:23.466Z\"," +
            "\"error_code\":1001," +
            "\"error_message\":\"mock error.\"," +
            "\"elapsed\":0," +
            "\"credit_count\":0" +
            "}" +
            "}"
        val expected = "mock error."
        val actual = sut.fromJson(json)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun mapErrorFromEmptyJsonFallbackTest() {
        val expected = RequestConstants.DEFAULT_ERROR
        val actual = sut.fromJson("")

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun mapErrorFromUnexpectedJsonFallbackTest() {
        val expected = RequestConstants.DEFAULT_ERROR
        val actual = sut.fromJson("{}")

        assertThat(actual).isEqualTo(expected)
    }

    private class MockCall : CryptoCall<String>() {
        override fun clone() = throw Exception("Mock")
        override fun execute() = throw Exception("Mock")
        override fun enqueue(callback: Callback<Result<String>>) = throw Exception("Mock")
        override fun isExecuted() = throw Exception("Mock")
        override fun cancel() = throw Exception("Mock")
        override fun isCanceled() = throw Exception("Mock")
        override fun request() = throw Exception("Mock")
        override fun timeout() = throw Exception("Mock")

        fun fromException(exception: Exception) = mapError(exception)
        fun fromJson(json: String) = mapError(json)
    }
}
