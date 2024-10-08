package com.agnes.multimedia.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agnes.multimedia.model.PhotoResponse
import com.agnes.multimedia.repository.PhotoRepository
import kotlinx.coroutines.launch

class PhotosViewModel: ViewModel() {
    val photoRepo = PhotoRepository()
    val errorLiveData = MutableLiveData<String>()
    val photoResponseLiveData = MutableLiveData<PhotoResponse>()

    fun postPhoto(uri: Uri,caption:String){
        viewModelScope.launch {
            val response = photoRepo.uploadPhoto(uri, caption)
            if(response.isSuccessful){
                photoResponseLiveData.postValue(response.body())
            }else{
                errorLiveData.postValue(response.errorBody()?.string())
            }
        }
    }
}