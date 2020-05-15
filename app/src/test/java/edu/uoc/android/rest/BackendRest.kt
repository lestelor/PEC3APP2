package edu.uoc.android.rest

import edu.uoc.android.Utils
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


//import kotlinx.android.synthetic.main.activity_museum.*;
class BackendRest {
    var mockServer = MockWebServer()
    private lateinit var apiMuseums: MuseumService
    //private lateinit var apiMuseums2: MuseumService2

    @Before
    @Throws(Exception::class)
    fun init() {
        MockitoAnnotations.initMocks(this)
        val mockServer = MockWebServer()
        mockServer.start()
    }

    @After
    fun teardown() {
        mockServer.shutdown()
    }


    @Test
    fun testMuseums() {
        val url: URL = mockServer.url("/api/dataset/museus/").toUrl()

        mockServer.enqueue(MockResponse().setBody(Utils.BODY))
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val `in`: InputStream = connection.inputStream
        val response: String = String(`in`.readBytes())
        Assert.assertEquals(Utils.BODY, response)
    }

    @Test
    fun testMuseumsRetrofit() {

        val response = MockResponse()
            .setBody(Utils.BODY)

        mockServer.enqueue(response)

        val apiMuseums = Retrofit.Builder()
            .baseUrl(mockServer.url(""))
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MuseumService::class.java)

        val responseApi = apiMuseums.museums("1","10").execute().body()

        Assert.assertEquals("Museu del Prat. Sala d'exposicions. Cèntric Espai Cultural",
            responseApi?.elements?.get(0)?.adrecaNom)

        Assert.assertEquals(1,
            responseApi?.elements?.size)
    }

}