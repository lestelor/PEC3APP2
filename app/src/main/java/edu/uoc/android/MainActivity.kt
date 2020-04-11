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
    }
}