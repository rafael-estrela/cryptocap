package br.eti.rafaelcouto.cryptocap.application.network.adapter

import br.eti.rafaelcouto.cryptocap.application.network.call.ArrayCall
import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Types
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Call

@RunWith(JUnit4::class)
class ArrayCallAdapterTest {

    @MockK private lateinit var mockCall: Call<Body<List<String>>>

    private lateinit var sut: ArrayCallAdapter<List<String>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val type = Types.newParameterizedType(List::class.java, String::class.java)
        sut = ArrayCallAdapter(responseType = type)
    }

    @Test
    fun getResponseCodeTest() {
        val expected = Types.newParameterizedType(List::class.java, String::class.java)
        val actual = sut.responseType()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun adaptCallTest() {
        val actual = sut.adapt(mockCall)

        assertThat(actual).isInstanceOf(ArrayCall::class.java)
    }
}
