package br.eti.rafaelcouto.cryptocap.application.network.call

import br.eti.rafaelcouto.cryptocap.application.network.RequestConstants
import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.testhelper.factory.BodyFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

@RunWith(JUnit4::class)
class ArrayCallTest {

    @MockK private lateinit var mockType: Type
    @MockK private lateinit var mockRequest: Request
    @MockK private lateinit var mockResponse: Response<Body<List<String>>>
    @MockK private lateinit var mockErrorBody: ResponseBody

    @RelaxedMockK private lateinit var mockDelegate: Call<Body<List<String>>>
    @RelaxedMockK private lateinit var mockCallback: Callback<Result<List<String>>>

    private lateinit var sut: ArrayCall<List<String>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = ArrayCall(delegate = mockDelegate, successType = mockType)

        every { mockDelegate.clone() }.returns(mockDelegate)
        every { mockDelegate.isExecuted }.returns(true)
        every { mockDelegate.isCanceled }.returns(true)
        every { mockDelegate.request() }.returns(mockRequest)
        every { mockDelegate.timeout() }.returns(Timeout.NONE)
    }

    @Test
    fun enqueueSuccessTest() = runBlocking {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(true)
        every { mockResponse.body() }.returns(BodyFactory.successBody(listOf("dummy data")))

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.success(listOf("dummy data")))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueNonSuccessfulTest() = runBlocking {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(false)
        every { mockResponse.body() }.returns(BodyFactory.successBody(listOf("dummy data")))
        every { mockResponse.errorBody() }.returns(mockErrorBody)
        every { mockErrorBody.string() }.returns("")

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<List<String>>(RequestConstants.DEFAULT_ERROR))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueNullBodyTest() = runBlocking {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(true)
        every { mockResponse.body() }.returns(null)
        every { mockResponse.errorBody() }.returns(mockErrorBody)
        every { mockErrorBody.string() }.returns("")

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<List<String>>(RequestConstants.DEFAULT_ERROR))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueNullErrorBodyTest() = runBlocking {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(false)
        every { mockResponse.body() }.returns(null)
        every { mockResponse.errorBody() }.returns(null)

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<List<String>>(RequestConstants.DEFAULT_ERROR))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueValidErrorBodyTest() = runBlocking {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(true)
        every { mockResponse.body() }.returns(null)
        every { mockResponse.errorBody() }.returns(mockErrorBody)
        every { mockErrorBody.string() }.returns("")

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<List<String>>(RequestConstants.DEFAULT_ERROR))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueExceptionTest() = runBlocking {
        every { mockDelegate.execute() }.throws(Exception("dummy exception"))

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<List<String>>("dummy exception"))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun cloneTest() {
        val actual = sut.clone()

        assertThat(actual).isInstanceOf(ArrayCall::class.java)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun executeTest() {
        sut.execute()
    }

    @Test
    fun isExecutedTest() {
        assertThat(sut.isExecuted).isEqualTo(mockDelegate.isExecuted)
    }

    @Test
    fun cancelTest() {
        sut.cancel()

        verify { mockDelegate.cancel() }
    }

    @Test
    fun isCanceledTest() {
        assertThat(sut.isCanceled).isEqualTo(mockDelegate.isCanceled)
    }

    @Test
    fun requestTest() {
        assertThat(sut.request()).isEqualTo(mockDelegate.request())
    }

    @Test
    fun timeoutTest() {
        assertThat(sut.timeout()).isEqualTo(mockDelegate.timeout())
    }
}
