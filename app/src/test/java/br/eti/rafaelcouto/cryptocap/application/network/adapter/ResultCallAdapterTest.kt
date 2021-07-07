package br.eti.rafaelcouto.cryptocap.application.network.adapter

import br.eti.rafaelcouto.cryptocap.application.network.call.ResultCall
import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Call

@RunWith(JUnit4::class)
class ResultCallAdapterTest {

    @MockK private lateinit var mockCall: Call<Body<String>>
    private lateinit var sut: ResultCallAdapter<String>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val type = String::class.java
        sut = ResultCallAdapter(type)
    }

    @Test
    fun getResponseCodeTest() {
        val actual = sut.responseType()

        assertThat(actual).isEqualTo(String::class.java)
    }

    @Test
    fun adaptCallTest() {
        val actual = sut.adapt(mockCall)

        assertThat(actual).isInstanceOf(ResultCall::class.java)
    }
}
