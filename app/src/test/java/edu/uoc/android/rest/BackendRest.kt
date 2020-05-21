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
import org.mockito.internal.matchers.Contains
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


//import kotlinx.android.synthetic.main.activity_museum.*;
class BackendRest {
    var mockServer = MockWebServer()

    // the mockServer is simulated here
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
    // The first test the Utils.BODY is enqueued as response to the server. Additionally the response is
    // received though a Retrofit in form of a Museum class. Then it is compared one element of the class with the actual value
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

    // In the second test the enqueued answer is compared with the actual value
    @Test
    fun testMuseumsEnqueue() {
        val url: URL = mockServer.url("/api/dataset/museus/").toUrl()
        val request = MockResponse()
            .setBody(Utils.BODY)
        mockServer.enqueue(request)


        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val `in`: InputStream = connection.inputStream
        val response: String = String(`in`.readBytes())
        Assert.assertEquals(Utils.BODY, response)
    }
    // The third test uses a dispatcher that sets as a body Utils.BODY and responsecode=200 when the path is /api/dataset/museus/
    @Test
    fun testMuseumsDispatcher() {
        val url: URL = mockServer.url("/api/dataset/museus/").toUrl()
        val mdispatcher: Dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                when (request.path) {
                    "/api/dataset/museus/" -> return MockResponse().setResponseCode(200).setBody(Utils.BODY)
                }
                return MockResponse().setResponseCode(404)
            }
        }
        mockServer.dispatcher= mdispatcher
        // connection.responseCode = 200 if url = "/api/dataset/museus/". The dispatcher returns Utils.BODY
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val `in`: InputStream = connection.inputStream
        val response: String = String(`in`.readBytes())
        Assert.assertEquals(Utils.BODY, response)
    }
}