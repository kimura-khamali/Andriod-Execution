package com.agnes.multimedia.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationRequest
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agnes.multimedia.R
import com.agnes.multimedia.databinding.ActivityPhotoCaptureBinding
import java.io.File

class PhotoCaptureActivity : AppCompatActivity() {
    lateinit var photoFile : File

    var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == Activity.RESULT_OK){
            val capturedPhoto = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.ivPhoto2.setImageBitmap(capturedPhoto)
        }
    }

    lateinit var binding: ActivityPhotoCaptureBinding

    val locationRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){result ->
        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            getLocation()
        }else{
            Toast.makeText(this,"Provide location permission", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    private fun getPhotoFile(filename : String): File{
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg",storageDir)
    }

    override fun onResume() {
        super.onResume()
        binding.btnCapture.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile("photo_${System.currentTimeMillis()}")
            val fileProvider = FileProvider.getUriForFile(this,"com.agnes.multimedia.provider",photoFile)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
            cameraLauncher.launch(cameraIntent)
        }
    }
    fun getLocation(){
//        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
//        val requestUserLocation = LocationRequest.Builder(10000).build()
//    }
}