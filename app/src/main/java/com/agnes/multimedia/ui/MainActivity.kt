package com.agnes.multimedia.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agnes.multimedia.R
import com.agnes.multimedia.databinding.ActivityMainBinding
import com.agnes.multimedia.model.PhotoResponse
import com.agnes.multimedia.utils.Constants
import com.agnes.multimedia.viewmodel.PhotosViewModel
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    val photosViewModel : PhotosViewModel by viewModels()
    lateinit var binding : ActivityMainBinding
    lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>
    var photoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
            if(uri != null){
                photoUri = uri
            }
        }
        binding.btUpload.setOnClickListener {
            validateForm()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.ivPhoto.setOnClickListener {
            val mimeType = "image/*"
            pickMedia.launch(PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.SingleMimeType(
                    mimeType
                )
            ))
        }
        photosViewModel.errorLiveData.observe(this){
                error->
            Toast.makeText(this,error,Toast.LENGTH_LONG)
        }
        photosViewModel.photoResponseLiveData.observe(this){photoResponse ->
            val imageUrl = "${Constants.BASEURL}${photoResponse.image}"
            Picasso.get()
                .load(imageUrl)
                .into(binding.ivPhoto)
        }
    }
    private fun validateForm(){
        var err = false
        if(photoUri == null){
            err = true
            Toast.makeText(this,"PLease select photo", Toast.LENGTH_LONG).show()
        }
        val caption = binding.etCaption.text.toString()
        if(caption.isBlank()){
            err = true
            binding.etCaption.error =" Caption required"
        }
        if(!err){
            photosViewModel.postPhoto(photoUri!!,caption)
        }

    }
}