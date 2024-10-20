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

    // Expose LiveData to the UI
    val post: LiveData<ResponseServices?>
        get() = _post

    val getDataDiri: LiveData<ResponseCollection?>
        get() = _getDataDiri

    init {
        // Load initial data
        getDataDiri()
    }

    // Function to post new data
    fun postDataDiri(name: RequestBody, address: RequestBody, outstanding: RequestBody) {
        NetworkConfig().getServiceCollection().addCollect(name, address, outstanding).enqueue(object :
            Callback<ResponseServices> {
            override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                if (response.isSuccessful) {
                    _post.postValue(response.body())
                    getDataDiri() // Refresh the list after posting
                } else {
                    showToast("Failed to upload data")
                }
            }

            override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    // Function to fetch data
    fun getDataDiri() {
        NetworkConfig().getServiceCollection().getAllCollect()
            .enqueue(object : Callback<ResponseCollection> {
                override fun onResponse(call: Call<ResponseCollection>, response: Response<ResponseCollection>) {
                    if (response.isSuccessful) {
                        _getDataDiri.postValue(response.body())
                    } else {
                        showToast("Failed to fetch data")
                    }
                }

                override fun onFailure(call: Call<ResponseCollection>, t: Throwable) {
                    showToast("Failed to fetch data: ${t.message}")
                }
            })
    }

    // Function to delete an item
    fun deleteCollect(id: String?) {
        id?.let {
            NetworkConfig().getServiceCollection().deleteCollect(it)
                .enqueue(object : Callback<ResponseServices> {
                    override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                        if (response.isSuccessful) {
                            showToast("Item deleted successfully")
                            getDataDiri() // Refresh the list after deletion
                        } else {
                            showToast("Failed to delete item")
                        }
                    }

                    override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                        showToast("Error: ${t.message}")
                    }
                })
        }
    }

    // Function to update an item
    fun updateCollect(id: String, name: RequestBody, address: RequestBody, outstanding: RequestBody) {
        NetworkConfig().getServiceCollection().updateCollect(id, name, address, outstanding).enqueue(object :
            Callback<ResponseServices> {
            override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                if (response.isSuccessful) {
                    _post.postValue(response.body())
                    getDataDiri() // Refresh the list after updating
                } else {
                    showToast("Failed to update item")
                }
            }

            override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    // Helper function to show Toast messages
    private fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}
