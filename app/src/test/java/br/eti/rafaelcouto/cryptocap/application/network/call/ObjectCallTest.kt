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
import okhttp3.HttpUrl
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
class ObjectCallTest {

    @MockK private lateinit var mockType: Type
    @MockK private lateinit var mockRequest: Request
    @MockK private lateinit var mockUrl: HttpUrl
    @MockK private lateinit var mockResponse: Response<Body<Map<String, String>>>
    @MockK private lateinit var mockErrorBody: ResponseBody

    @RelaxedMockK private lateinit var mockDelegate: Call<Body<Map<String, String>>>
    @RelaxedMockK private lateinit var mockCallback: Callback<Result<String>>

    private lateinit var sut: ObjectCall<String>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = ObjectCall(delegate = mockDelegate, successType = mockType)

        every { mockDelegate.clone() }.returns(mockDelegate)
        every { mockDelegate.isExecuted }.returns(true)
        every { mockDelegate.isCanceled }.returns(true)
        every { mockDelegate.request() }.returns(mockRequest)
        every { mockDelegate.timeout() }.returns(Timeout.NONE)

        every { mockRequest.url }.returns(mockUrl)
        every { mockUrl.queryParameter(any()) }.returns("1")
    }

    @Test
    fun enqueueSuccessTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(true)
        every { mockResponse.body() }.returns(BodyFactory.successBody(mapOf("1" to "dummy data")))

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.success("dummy data"))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)

        verify { mockUrl.queryParameter(ObjectCall.ID_QUERY_PARAM) }
    }

    @Test
    fun enqueueNonSuccessfulTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(false)
        every { mockResponse.body() }.returns(BodyFactory.successBody(mapOf("1" to "dummy data")))
        every { mockResponse.errorBody() }.returns(mockErrorBody)
        every { mockErrorBody.string() }.returns("")

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<String>(RequestConstants.DEFAULT_ERROR))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueNullBodyTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(true)
        every { mockResponse.body() }.returns(null)
        every { mockResponse.errorBody() }.returns(mockErrorBody)
        every { mockErrorBody.string() }.returns("")

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<String>(RequestConstants.DEFAULT_ERROR))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueNullErrorTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(false)
        every { mockResponse.body() }.returns(null)
        every { mockResponse.errorBody() }.returns(null)

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<String>(RequestConstants.DEFAULT_ERROR))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueValidErrorTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(true)
        every { mockResponse.body() }.returns(null)
        every { mockResponse.errorBody() }.returns(mockErrorBody)
        every { mockErrorBody.string() }.returns("")

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<String>(RequestConstants.DEFAULT_ERROR))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueExceptionTest() {
        every { mockDelegate.execute() }.throws(Exception("dummy exception"))

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse = Response.success(Result.error<String>("dummy exception"))
            val actualResponse = args.last() as Response<*>

            assertThat(args.first()).isEqualTo(sut)
            assertThat(actualResponse.body()).isEqualTo(expectedResponse.body())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun cloneTest() {
        val actual = sut.clone()

        assertThat(actual).isInstanceOf(ObjectCall::class.java)
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
