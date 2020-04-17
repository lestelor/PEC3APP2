package edu.uoc.android


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.uoc.android.models.Element
import edu.uoc.android.models.Museums
import edu.uoc.android.rest.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    class Museucoordenades (var nom:String, var coordenades: String)
    var llistacoordenades : MutableList<Museucoordenades> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val mMyApp = this.applicationContext


        // Find the user location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if(checkPermissions()) {
            mMap.isMyLocationEnabled = true
        }

        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                val centroMapa= location?.let { onLocationChanged(it) }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centroMapa,9.0f))
            }
            .addOnFailureListener { e ->
                Log.d("MapDemoActivity", "Error trying to get last GPS location")
                e.printStackTrace()
            }


        // This part calls the API and returns the museum's name and coordinates
        val museuService = RetrofitFactory()


        museuService.museums("1","10")?.enqueue(object : Callback<Museums> {
            override fun onResponse(call: Call<Museums>, response: Response<Museums>) {
                if (response.code() == 200) {
                    val museums = response.body()!!
                    val getelements = museums.getElements()


                    llistacoordenades  = getllistacoordenades(getelements)
                    val encontrar= ","
                    for(i in 0 until llistacoordenades.size) {
                        var posicion_separador= llistacoordenades[i].coordenades.indexOf(encontrar,0)
                        var utmx=llistacoordenades[i].coordenades.take(posicion_separador).toDouble()
                        var utmY=llistacoordenades[i].coordenades.takeLast(llistacoordenades[i].coordenades.length - posicion_separador-1).toDouble()
                        var marcador = LatLng(utmx,utmY)
                        mMap.addMarker(MarkerOptions()
                            .position(marcador)
                            .title(llistacoordenades[i].nom)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.museum_24)))
                    }
                }
            }

            override fun onFailure(call: Call<Museums>, t: Throwable) {
                Log.d("Tipus resposta 10","Failure")
            }


        })






    }
    private fun getllistacoordenades(getelements: MutableList<Element>) : MutableList<Museucoordenades> {
        val llistacoordenades = mutableListOf(Museucoordenades(nom=getelements[0].adrecaNom,coordenades= getelements[0].localitzacio.toString()))
        if (getelements.size > 1) {
            for (i in 1..getelements.size-1) {
                llistacoordenades.add(
                    Museucoordenades(
                        nom = getelements[i]?.adrecaNom,
                        coordenades = getelements[i]?.localitzacio
                    )
                )
            }
        }
        return llistacoordenades
    }
    private fun onLocationChanged(location: Location): LatLng {
        // New location has now been determined

        // You can now create a LatLng Object for use with maps
        val latLng = LatLng(location.latitude, location.longitude)
        return latLng
    }

    private fun checkPermissions(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            requestPermissions()
            false
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FINE_LOCATION
        )
    }



    companion object {
        private val REQUEST_FINE_LOCATION = 1
    }

}
