package edu.uoc.android

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1
    val folder = "UOCImageAPP/"
    val imageTheFile = "imageApp.jpg"
    var currentPhotoPath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        fabuttonSettings?.setOnClickListener { dispatchTakePictureIntent() }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("seguimiento", "foto requestcode $requestCode")
        Log.d("seguimiento", "foto resultcode $resultCode")


        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            this.ivSetting.setImageBitmap(imageBitmap)
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.toString() + File.separator + folder
            Log.d("seguimiento", "foto storageDir $storageDir")
            val storageDirFile = File(storageDir)
            storageDirFile.mkdirs()
            val imageFile = File(storageDirFile, imageTheFile)
            val stream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val fos = FileOutputStream(imageFile)
            fos.write(stream.toByteArray())
            fos.close()
            this.ivSetting.setImageBitmap(imageBitmap)
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val aaa=Environment.getExternalStorageDirectory().toString()
        Log.d("seguimiento", "foto directorio environment $aaa")
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.d("seguimiento", "foto directorio $storageDir")

        return File.createTempFile(
            "imageapp", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
            ).apply {
            // Save a file: path for use with ACTION_VIEW intents

            currentPhotoPath = absolutePath
        }
    }


}


