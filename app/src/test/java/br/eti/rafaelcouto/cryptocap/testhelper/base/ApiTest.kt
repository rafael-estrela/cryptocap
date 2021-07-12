package br.eti.rafaelcouto.cryptocap.testhelper.base

import br.eti.rafaelcouto.cryptocap.application.network.RequestConstants
import br.eti.rafaelcouto.cryptocap.application.network.adapter.ResultAdapterFactory
import br.eti.rafaelcouto.cryptocap.application.network.interceptor.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

@Suppress("UnnecessaryAbstractClass")
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

    protected fun enqueueResponseFromFile(path: String?, code: Int = HttpURLConnection.HTTP_OK) {
        val body = getJson(path)
        val response = MockResponse().setResponseCode(code).setBody(body)

        mockWebServer.enqueue(response)
    }

    protected inline fun <reified T> buildApi(): T = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/").toString())
        .client(buildClient())
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(ResultAdapterFactory())
        .build().create(T::class.java)

    protected fun buildClient() = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .callTimeout(RequestConstants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .build()

    private fun getJson(path: String?) = path?.let {
        val uri = javaClass.classLoader?.getResource(it) ?: return@let ""
        val file = File(uri.path)

        String(file.readBytes())
    }.orEmpty()
}
