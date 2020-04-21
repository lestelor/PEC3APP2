package edu.uoc.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        relay_museums.setOnClickListener{
            val intent =Intent(this,ActivityMuseum::class.java)
            startActivity(intent)
        }

        relay_maps.setOnClickListener{
            val intent =Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }


        relay_quizzes.setOnClickListener{
            val intent =Intent(this,QuizzesActivity::class.java)
            startActivity(intent)
        }

        relay_setting.setOnClickListener{
            val intent =Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }

    }
}