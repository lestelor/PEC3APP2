package edu.uoc.android.fauliclaudi.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun  RetrofitFactory(): MuseumService {
    val retrofit = Retrofit.Builder()
        .baseUrl(APIConstants.API_URL)
        //.client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(MuseumService::class.java)

}