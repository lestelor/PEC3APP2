package edu.uoc.android

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.android.R.layout.activity_museum
import edu.uoc.android.models.Element
import edu.uoc.android.models.Museums
import edu.uoc.android.rest.MuseuAdapter
import edu.uoc.android.rest.Museubanner
import edu.uoc.android.rest.RetrofitFactory
import kotlinx.android.synthetic.main.activity_museum.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityMuseum : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_museum)

        // llistamuseus contains the list of museums defined in the class MuseumService, which
        // contains the name and image of each museum
        var llistamuseus: MutableList<Museubanner>
        // Definition of the adaptor which calls the API MuseumService to get the objects in an specific URL
        // and with the format specified in models-> Museums
        val museuService = RetrofitFactory()

        // Safe call of the funtion included in the API, which retrieves elements from pagIni to PagFi
        // Response code 200 in http correspond to ok
        museuService.museums("1","10")?.enqueue(object : Callback<Museums> {
            override fun onResponse(call: Call<Museums>, response: Response<Museums>) {
                if (response.code() == 200) {
                    indeterminateBar.setVisibility(View.GONE)
                    val museums = response.body()!!
                    val getelements = museums.getElements()
                    llistamuseus=getllistamuseus(getelements)

                    // the recyclerView is attaches to the list_item layout
                    // We call the adapter to fill the list_item as many times as the number
                    // of elements of llistamuseus

                    val recyclerView = rv_museums
                    recyclerView.layoutManager = LinearLayoutManager(this@ActivityMuseum)
                    recyclerView.adapter =  MuseuAdapter(llistamuseus, this@ActivityMuseum)
                }
            }

            override fun onFailure(call: Call<Museums>, t: Throwable) {
                Log.d("Tipus resposta 10","Failure")
            }


        })

    }

    private fun getllistamuseus(getelements: MutableList<Element>) : MutableList<Museubanner> {
        val llistamuseus: MutableList<Museubanner> = mutableListOf()
        if (getelements.size > 0) {
            for (i in 1..getelements.size-1) {
                var q = Museubanner(getelements[i]?.adrecaNom,  url = getelements[i]?.imatge[0].toString())
                llistamuseus.add(q)
            }
        }
        return llistamuseus
    }
}



