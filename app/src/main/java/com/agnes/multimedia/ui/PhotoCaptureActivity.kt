package com.agnes.multimedia.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.agnes.multimedia.databinding.ActivityPhotoCaptureBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File

class PhotoCaptureActivity : AppCompatActivity() {
    private lateinit var photoFile: File
    private lateinit var binding: ActivityPhotoCaptureBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Camera launcher to capture the photo and set it to ImageView
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val capturedPhoto = BitmapFactory.decodeFile(photoFile.absolutePath)
                binding.ivPhoto2.setImageBitmap(capturedPhoto)
            }
        }

    // Permission launcher for requesting location permissions
    private val locationRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocationUpdates()
        } else {
            Toast.makeText(this, "Please grant location permission", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FusedLocationProviderClient using LocationServices
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Handle capture button click
        binding.btnCapture.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile("photo_${System.currentTimeMillis()}")
            val fileProvider =
                FileProvider.getUriForFile(this, "com.agnes.multimedia.provider", photoFile)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            cameraLauncher.launch(cameraIntent)
        }
    }

    // Helper function to create a temporary file for the captured photo
    private fun getPhotoFile(filename: String): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg", storageDir)
    }

    // Function to start location updates
    private fun getLocationUpdates(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = com.google.android.gms.location.LocationRequest.Builder(10000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val lastLocation = locationResult.lastLocation
                    if (lastLocation != null) {
                        Toast.makeText(
                            this@PhotoCaptureActivity,
                            "${lastLocation.longitude}, ${lastLocation.latitude}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                mainLooper
            )
            true
        } else {
            locationRequestLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            false
        }
    }
}

















//package com.agnes.multimedia.ui
//
//import android.Manifest
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.location.LocationRequest
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.agnes.multimedia.R
//import com.agnes.multimedia.databinding.ActivityPhotoCaptureBinding
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.Priority
//import java.io.File
//
//class PhotoCaptureActivity : AppCompatActivity() {
//    lateinit var photoFile: File
//
//    var cameraLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val capturedPhoto = BitmapFactory.decodeFile(photoFile.absolutePath)
//                binding.ivPhoto2.setImageBitmap(capturedPhoto)
//            }
//        }
//
//    lateinit var binding: ActivityPhotoCaptureBinding
//    lateinit var fusedLocationProviderClient:FusedLocationProviderClient
//
//    val locationRequest = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { result ->
//        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getLocationUpdates()
//        }
////        else {
////            Toast.makeText(this, "Please grant location permission", Toast.LENGTH_LONG).show()
////        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPhotoCaptureBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//    }
//
//    private fun getPhotoFile(filename: String): File {
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(filename, ".jpg", storageDir)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        binding.btnCapture.setOnClickListener {
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            photoFile = getPhotoFile("photo_${System.currentTimeMillis()}")
//            val fileProvider =
//                FileProvider.getUriForFile(this, "com.agnes.multimedia.provider", photoFile)
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
//            cameraLauncher.launch(cameraIntent)
//        }
//    }
//
//    private fun getLocationUpdates() {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
//        {val locationRequest = com.google.android.gms.location.LocationRequest
//            .Builder(10000)
//            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//            .build()
//
//            val locationCallback = object:LocationCallback(){
//                override fun onLocationResult(p0: LocationResult) {
//                    super.onLocationResult(p0)
//                    val lastLocation = p0.lastLocation
//                    if(lastLocation!=null){
//                        Toast.makeText(this@PhotoCaptureActivity,"${lastLocation.longitude},${lastLocation.latitude}",Toast.LENGTH_LONG)
//                    },show()
//                }
//
//            }            }
//
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Toast.getMainLooper)
////        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
////        val requestUserLocation = LocationRequest.Builder(10000).build()
////    }
//    }else{
//        locationRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
//    }
//}