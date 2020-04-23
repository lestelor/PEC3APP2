package edu.uoc.android


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class SettingsActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val folder = "UOCImageAPP/"
    val imageTheFile = "imageApp.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imgFile = File(getStorageDir() + File.separator + imageTheFile)

        if (checkPermissions() && imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            ivSetting.setImageBitmap(myBitmap)
        } else {
            tvSetting.text = getString(R.string.noImage)
        }

        fabuttonSettings?.setOnClickListener { dispatchTakePictureIntent() }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("seguimiento", "foto requestcode $requestCode")
        Log.d("seguimiento", "foto resultcode $resultCode")


        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            this.ivSetting.setImageBitmap(imageBitmap)

            val storageDir=getStorageDir()

            Log.d("seguimiento", "foto storageDir $storageDir")
            val storageDirFile = File(storageDir)
            if (!storageDirFile.exists()) {
                storageDirFile.mkdirs()
            }
            val imageFile = File(storageDirFile, imageTheFile)
            val stream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val fos = FileOutputStream(imageFile)
            fos.write(stream.toByteArray())
            fos.close()
            Toast.makeText(applicationContext,getString(R.string.pictureSaved),Toast.LENGTH_SHORT).show()
            tvSetting.text = ""
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    private fun getStorageDir(): String {
        return getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.toString() + File.separator + folder
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
            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_EXTERNAL_STORAGE
        )
    }



    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
    }

}


