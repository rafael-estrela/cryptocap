package br.eti.rafaelcouto.cryptocap

import br.eti.rafaelcouto.cryptocap.application.network.adapter.ResponseAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

@RunWith(JUnit4::class)
abstract class ApiTest {

    protected lateinit var mockWebServer: MockWebServer

    @Before
    fun baseSetUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun baseTearDown() {
        mockWebServer.shutdown()
    }

    protected fun enqueueResponseFromFile(path: String?, code: Int = 200) {
        val body = getJson(path)
        val response = MockResponse().setResponseCode(code).setBody(body)

        mockWebServer.enqueue(response)
    }

    protected inline fun <reified T> buildApi(): T = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/").toString())
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(ResponseAdapterFactory())
        .build().create(T::class.java)

    private fun getJson(path: String?) = path?.let {
        val uri = javaClass.classLoader?.getResource(it) ?: return@let ""
        val file = File(uri.path)

        String(file.readBytes())
    }.orEmpty()
}
