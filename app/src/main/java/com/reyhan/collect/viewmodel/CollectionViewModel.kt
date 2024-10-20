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

    val post: LiveData<ResponseServices?>
        get() = _post

    val getDataDiri: LiveData<ResponseCollection?>
        get() = _getDataDiri

    init {
        getDataDiri()
    }

    fun postDataDiri(name: RequestBody, address: RequestBody, outstanding: RequestBody) {
        NetworkConfig().getServiceCollection().addCollect(name, address, outstanding).enqueue(object :
            Callback<ResponseServices> {
            override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                if (response.isSuccessful) {
                    _post.postValue(response.body())
                    getDataDiri() // Refresh the list after posting
                } else {
                    Toast.makeText(getApplication(), "Failed to upload data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                Toast.makeText(getApplication(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getDataDiri() {
        NetworkConfig().getServiceCollection().getAllCollect()
            .enqueue(object : Callback<ResponseCollection> {
                override fun onResponse(call: Call<ResponseCollection>, response: Response<ResponseCollection>) {
                    _getDataDiri.postValue(response.body())
                }

                override fun onFailure(call: Call<ResponseCollection>, t: Throwable) {
                    Toast.makeText(getApplication(), "Failed to fetch data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun deleteCollect(id: String?) {
        id?.let {
            NetworkConfig().getServiceCollection().deleteCollect(it)
                .enqueue(object : Callback<ResponseServices> {
                    override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                        if (response.isSuccessful) {
                            Toast.makeText(getApplication(), "Item deleted successfully", Toast.LENGTH_SHORT).show()
                            getDataDiri() // Refresh the list after deletion
                        } else {
                            Toast.makeText(getApplication(), "Failed to delete item", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                        Toast.makeText(getApplication(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun updateCollect(id: String, name: RequestBody, address: RequestBody, outstanding: RequestBody) {
        NetworkConfig().getServiceCollection().updateCollect(id, name, address, outstanding).enqueue(object :
            Callback<ResponseServices> {
            override fun onResponse(call: Call<ResponseServices>, response: Response<ResponseServices>) {
                if (response.isSuccessful) {
                    _post.postValue(response.body())
                    getDataDiri() // Refresh the list after updating
                } else {
                    Toast.makeText(getApplication(), "Failed to update item", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseServices>, t: Throwable) {
                Toast.makeText(getApplication(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
