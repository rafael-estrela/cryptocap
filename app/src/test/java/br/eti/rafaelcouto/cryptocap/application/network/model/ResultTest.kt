package br.eti.rafaelcouto.cryptocap.application.network.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ResultTest {

    @Test
    fun createLoadingResultTest() {
        val actual = Result.loading<String>()

        assertThat(actual.status).isEqualTo(Result.Status.LOADING)
        assertThat(actual.error).isNull()
        assertThat(actual.data).isNull()
    }

    @Test
    fun createSuccessResultTest() {
        val actual = Result.success(data = "dummy data")

        assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
        assertThat(actual.error).isNull()
        assertThat(actual.data).isEqualTo("dummy data")
    }

    @Test
    fun createErrorResultTest() {
        val actual = Result.error<String>(error = "dummy error")

        assertThat(actual.status).isEqualTo(Result.Status.ERROR)
        assertThat(actual.error).isEqualTo("dummy error")
        assertThat(actual.data).isNull()
    }
}
