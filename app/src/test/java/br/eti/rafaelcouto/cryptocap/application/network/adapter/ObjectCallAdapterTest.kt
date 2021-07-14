package br.eti.rafaelcouto.cryptocap.application.network.adapter

import br.eti.rafaelcouto.cryptocap.application.network.call.ObjectCall
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
class ObjectCallAdapterTest {

    @MockK private lateinit var mockCall: Call<Body<Map<String, String>>>

    private lateinit var sut: ObjectCallAdapter<String>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = ObjectCallAdapter(responseType = String::class.java)
    }

    @Test
    fun getResponseCodeTest() {
        val actual = sut.responseType()

        assertThat(actual).isEqualTo(String::class.java)
    }

    @Test
    fun adaptCallTest() {
        val actual = sut.adapt(mockCall)

        assertThat(actual).isInstanceOf(ObjectCall::class.java)
    }
}
