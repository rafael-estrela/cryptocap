package br.eti.rafaelcouto.cryptocap.application.network.adapter

import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Types
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Retrofit
import java.lang.Exception

class ResultAdapterFactoryTest {

    @MockK private lateinit var mockRetrofit: Retrofit

    private lateinit var sut: ResultAdapterFactory

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = ResultAdapterFactory.invoke()
    }

    @Test
    fun getNoUpperBoundAdapterTest() {
        val type = String::class.java

        val actual = sut.get(type, arrayOf(), mockRetrofit)

        assertThat(actual).isNull()
    }

    @Test
    fun getNullAdapterTest() {
        val type = Types.newParameterizedType(Call::class.java, String::class.java)

        val actual = sut.get(type, arrayOf(), mockRetrofit)

        assertThat(actual).isNull()
    }

    @Test(expected = IllegalStateException::class)
    fun getNonParameterizedTest() {
        val type = Types.newParameterizedType(Call::class.java, Result::class.java)

        try {
            sut.get(type, arrayOf(), mockRetrofit)
        } catch (e: Exception) {
            assertThat(e.message).isEqualTo("Response must be parameterized as Result<Foo> or Result<out Foo>")

            throw e
        }
    }

    @Test
    fun getObjectCallAdapterTest() {
        val innerType = Types.newParameterizedType(Result::class.java, String::class.java)
        val type = Types.newParameterizedType(Call::class.java, innerType)

        val expectedInnerType = Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
        val expectedType = Types.newParameterizedType(Body::class.java, expectedInnerType)
        val actual = sut.get(type, arrayOf(), mockRetrofit)

        assertThat(actual).isNotNull()
        assertThat(actual).isInstanceOf(ObjectCallAdapter::class.java)
        assertThat(actual?.responseType()).isEqualTo(expectedType)
    }

    @Test
    fun getArrayCallAdapterTest() {
        val mostInnerType = Types.newParameterizedType(List::class.java, String::class.java)
        val innerType = Types.newParameterizedType(Result::class.java, mostInnerType)
        val type = Types.newParameterizedType(Call::class.java, innerType)

        val expectedType = Types.newParameterizedType(Body::class.java, mostInnerType)
        val actual = sut.get(type, arrayOf(), mockRetrofit)

        assertThat(actual).isNotNull()
        assertThat(actual).isInstanceOf(ArrayCallAdapter::class.java)
        assertThat(actual?.responseType()).isEqualTo(expectedType)
    }
}
