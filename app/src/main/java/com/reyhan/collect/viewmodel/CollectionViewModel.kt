package com.reyhan.collect.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reyhan.collect.model.ResponseCollection
import com.reyhan.collect.model.ResponseServices
import com.reyhan.collect.services.NetworkConfig
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionViewModel(application: Application) : AndroidViewModel(application) {
    private val _post = MutableLiveData<ResponseServices?>()
    private val _getDataDiri = MutableLiveData<ResponseCollection?>()
    private val service = NetworkConfig().getServiceCollection() // Initialize the service once

    val post: LiveData<ResponseServices?>
        get() = _post

    val getDataDiri: LiveData<ResponseCollection?>
        get() = _getDataDiri

    init {
        getDataDiri() // Load initial data
    }

    fun postDataDiri(name: RequestBody, address: RequestBody, outstanding: RequestBody) {
        service.addCollect(name, address, outstanding).enqueue(object : Callback<ResponseServices> {
            override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                if (response.isSuccessful) {
                    _post.postValue(response.body())
                    getDataDiri() // Refresh the list after posting
                } else {
                    handleError("Failed to upload data")
                }
            }

            override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                handleError("Error: ${t.message}")
            }
        })
    }

    fun getDataDiri() {
        service.getAllCollect().enqueue(object : Callback<ResponseCollection> {
            override fun onResponse(call: Call<ResponseCollection>, response: Response<ResponseCollection>) {
                _getDataDiri.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseCollection>, t: Throwable) {
                handleError("Failed to fetch data: ${t.message}")
            }
        })
    }

    fun deleteCollect(id: RequestBody) {
        service.deleteCollect(id).enqueue(object : Callback<ResponseServices> {
            override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                if (response.isSuccessful) {
                    _post.postValue(response.body())
                    getDataDiri() // Refresh the list after updating
                } else {
                    handleError("Failed to update item")
                }
            }

            override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                handleError("Error: ${t.message}")
            }
        })
    }

    fun updateCollect(id: RequestBody, name: RequestBody, address: RequestBody, outstanding: RequestBody) {
        service.updateCollect(id, name, address, outstanding).enqueue(object : Callback<ResponseServices> {
            override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                if (response.isSuccessful) {
                    _post.postValue(response.body())
                    getDataDiri() // Refresh the list after updating
                } else {
                    handleError("Failed to update item")
                }
            }

            override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                handleError("Error: ${t.message}")
            }
        })
    }

    private fun handleError(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}
