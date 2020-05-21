package edu.uoc.android


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class SettingsActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val folder = "UOCImageAPP/"
    val imageTheFile = "imageApp.jpg"

    /**
     * The `FirebaseAnalytics` used to record screen views.
     */
    // [START declare_analytics]
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    // [END declare_analytics]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // [START shared_app_measurement]
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        // [END shared_app_measurement]

        firebaseAnalytics.setCurrentScreen(this, "Settings", "Nombre activity: " + javaClass.simpleName /* class override */)
        Log.d("Activity", "Nombre activity: " + javaClass.simpleName)

        val imgFile = File(getStorageDir() + File.separator + imageTheFile)

        // check permissions always true since minSDK 21 > manifest SDK version permision 18
        if (checkPermissionsExternal() && imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            ivSetting.setImageBitmap(myBitmap)
        } else {
            tvSetting.text = getString(R.string.noImage)
        }

        fabuttonSettings?.setOnClickListener { dispatchTakePictureIntent() }


    }


    // We receive the thumbnail of the picture and store in SDCARD as result of the camera intent

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("seguimiento", "foto requestcode $requestCode")
        Log.d("seguimiento", "foto resultcode $resultCode")

        // [START image_view_event]
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Captura de imagen")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
        // [END image_view_event]

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
            // Create the buffer
            val stream = ByteArrayOutputStream()
            // the compressed image is the same quality as the thumbnail of the image
            // Put the thumbnail in the buffer and save it
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val fos = FileOutputStream(imageFile)
            fos.write(stream.toByteArray())
            fos.close()
            // save the picture and remove the advisory comment
            Toast.makeText(applicationContext,getString(R.string.pictureSaved),Toast.LENGTH_SHORT).show()
            tvSetting.text = ""
        }
    }

    // Launch the photo app as an intent
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    // The sdcard directory is equal to the partition storage/emulated/0 which corresponds to the public external storage
    // See https://imnotyourson.com/which-storage-directory-should-i-use-for-storing-on-android-6/
    // The pricate external storage is storage/emulated/0/Android/data/edu.uoc.android
    // The pictures are in storage/emulated/0/Android/data/edu.uoc.android/files/Pictures/UOCImageAPP/
    // To save in other location needs to be analyzed if package_paths is to be used.

    private fun getStorageDir(): String {
        return getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.toString() + File.separator + folder
    }

    // Check camera permissions if the hardware SDK is < 18
    // Beyond sdk version 18 is no longer needed and it is the case that the min SDK v<to run the app is version 21

    private fun checkPermissionsExternal(): Boolean {
        Log.d("seguimiento", "foto versionsdkint ${Build.VERSION.SDK_INT}")
        Log.d("seguimiento", "foto versioncode ${Build.VERSION_CODES.P}")
        return if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) || (Build.VERSION.SDK_INT>18)){
            true
        } else {
            requestPermissionsExternal()
            false
        }
    }

    private fun requestPermissionsExternal() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_EXTERNAL_STORAGE
        )
    }

    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
    }

}


