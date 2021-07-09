package br.eti.rafaelcouto.cryptocap.application.network.call

import br.eti.rafaelcouto.cryptocap.application.network.model.Body
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.factory.BodyFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class ResultCallTest {

    @MockK private lateinit var mockType: Type
    @MockK private lateinit var mockRequest: Request
    @MockK private lateinit var mockResponse: Response<Body<String>>
    @MockK private lateinit var mockErrorBody: ResponseBody

    @RelaxedMockK private lateinit var mockDelegate: Call<Body<String>>
    @RelaxedMockK private lateinit var mockCallback: Callback<Result<String>>

    private lateinit var sut: ResultCall<String>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = ResultCall(delegate = mockDelegate, successType = mockType)

        every { mockDelegate.clone() }.returns(mockDelegate)
        every { mockDelegate.isExecuted }.returns(true)
        every { mockDelegate.isCanceled }.returns(true)
        every { mockDelegate.request() }.returns(mockRequest)
        every { mockDelegate.timeout() }.returns(Timeout.NONE)
    }

    @Test
    fun enqueueSuccessTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(true)
        every { mockResponse.body() }.returns(BodyFactory.successBody("dummy data"))

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse: Response<Result<String>> = Response.success(Result.success("dummy data"))

            assertThat(this.args.first()).isEqualTo(sut)
            assertThat(this.args.last().toString()).isEqualTo(expectedResponse.toString())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueNonSuccessfulTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(false)
        every { mockResponse.errorBody() }.returns(mockErrorBody)
        every { mockErrorBody.string() }.returns("")

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse: Response<Result<String>> = Response.success(Result.error(""))

            assertThat(this.args.first()).isEqualTo(sut)
            assertThat(this.args.last().toString()).isEqualTo(expectedResponse.toString())
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
            val expectedResponse: Response<Result<String>> = Response.success(Result.error(""))

            assertThat(this.args.first()).isEqualTo(sut)
            assertThat(this.args.last().toString()).isEqualTo(expectedResponse.toString())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueNullErrorBodyTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(false)
        every { mockResponse.errorBody() }.returns(null)

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse: Response<Result<String>> = Response.success(Result.error(""))

            assertThat(this.args.first()).isEqualTo(sut)
            assertThat(this.args.last().toString()).isEqualTo(expectedResponse.toString())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueValidErrorBodyTest() {
        every { mockDelegate.execute() }.returns(mockResponse)
        every { mockResponse.isSuccessful }.returns(true)
        every { mockResponse.body() }.returns(null)
        every { mockResponse.errorBody() }.returns(mockErrorBody)
        every { mockErrorBody.string() }.returns("{" +
            "\"status\":{" +
            "\"timestamp\":\"2021-07-06T19:10:23.466Z\"," +
            "\"error_code\":1001," +
            "\"error_message\":\"mock error.\"," +
            "\"elapsed\":0," +
            "\"credit_count\":0" +
            "}" +
            "}")

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse: Response<Result<String>> = Response.success(Result.error("mock error"))

            assertThat(this.args.first()).isEqualTo(sut)
            assertThat(this.args.last().toString()).isEqualTo(expectedResponse.toString())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueExceptionTest() {
        every { mockDelegate.execute() }.throws(Exception("dummy exception"))

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse: Response<Result<String>> = Response.success(Result.error("dummy exception"))

            assertThat(this.args.first()).isEqualTo(sut)
            assertThat(this.args.last().toString()).isEqualTo(expectedResponse.toString())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun enqueueExceptionNoMessageTest() {
        every { mockDelegate.execute() }.throws(Exception())

        every { mockCallback.onResponse(any(), any()) }.answers {
            val expectedResponse: Response<Result<String>> = Response.success(Result.error(""))

            assertThat(this.args.first()).isEqualTo(sut)
            assertThat(this.args.last().toString()).isEqualTo(expectedResponse.toString())
        }

        sut.enqueue(mockCallback)
    }

    @Test
    fun cloneTest() {
        val actual = sut.clone()

        assertThat(actual).isInstanceOf(ResultCall::class.java)
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
