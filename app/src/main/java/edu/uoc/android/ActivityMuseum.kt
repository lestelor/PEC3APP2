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
        val mMyApp = this.applicationContext
        var llistamuseus: MutableList<Museubanner> = mutableListOf()

        val museuService = RetrofitFactory()

        museuService.museums("1","10")?.enqueue(object : Callback<Museums> {
            override fun onResponse(call: Call<Museums>, response: Response<Museums>) {
                if (response.code() == 200) {
                    indeterminateBar.setVisibility(View.GONE)
                    val museums = response.body()!!
                    val getelements = museums.getElements()
                    llistamuseus=getllistamuseus(getelements)

                    val recyclerView = rv_museums
                    recyclerView.layoutManager = LinearLayoutManager(mMyApp)
                    recyclerView.adapter =  MuseuAdapter(llistamuseus, mMyApp)
                }
            }

            override fun onFailure(call: Call<Museums>, t: Throwable) {
                Log.d("Tipus resposta 10","Failure")
            }


        })

    }

    private fun getllistamuseus(getelements: MutableList<Element>) : MutableList<Museubanner> {
        val llistamuseus = mutableListOf(Museubanner(nom=getelements[0].adrecaNom,url=getelements[0].imatge[0].toString()))
        if (getelements.size > 1) {
            for (i in 1..getelements.size-1) {
                llistamuseus.add(
                    Museubanner(
                        nom = getelements[i]?.adrecaNom,
                        url = getelements[i]?.imatge[0].toString()
                    )
                )
            }
        }
        return llistamuseus
    }
}



