package edu.uoc.android.rest


import edu.uoc.android.models.Museums
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface MuseumService {


    @GET("/api/dataset/museus/format/json/pag-ini/{pagIni}/pag-fi/{pagFin}")
    fun museums(@Query("pagIni")pagIni:String,@Query("pagFin")pagFin:String): Call<Museums>

}
