package edu.uoc.android.fauliclaudi22.rest


import edu.uoc.android.fauliclaudi22.rest.models.Museums
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface MuseumService {


    @GET("/api/dataset/museus/format/json/pag-ini/{pagIni}/pag-fi/{pagFi}")
    fun museums(@Path("pagIni") pagIni: String, @Path("pagFi") pagFi: String): Call<Museums>

}
