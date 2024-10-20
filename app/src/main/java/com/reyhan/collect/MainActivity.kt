package com.reyhan.collect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reyhan.collect.adapter.CollectionAdapter
import com.reyhan.collect.model.CollectItem
import com.reyhan.collect.viewmodel.CollectionViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : AppCompatActivity() {
    private lateinit var btnAdd: Button
    private lateinit var btnSearch: Button
    private lateinit var txtSearch: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CollectionAdapter

    private val viewModel: CollectionViewModel by viewModels()

    // Store the original data list for search functionality
    private var originalDataList: List<CollectItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        btnAdd = findViewById(R.id.btnTambahData)
        btnSearch = findViewById(R.id.btnSearch)
        txtSearch = findViewById(R.id.txtSearch)
        recyclerView = findViewById(R.id.lstCollection)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CollectionAdapter(mutableListOf(), { item -> deleteCollect(item) }, { item -> editCollect(item) })
        recyclerView.adapter = adapter

        // Load data on start
        viewModel.getDataDiri()

        // Observe changes in data
        viewModel.getDataDiri.observe(this) { response ->
            if (response != null) {
                if (response.status == true && response.data?.collect != null) {
                    // Filter out null items and store in originalDataList
                    originalDataList = response.data.collect.filterNotNull()
                    adapter.updateData(originalDataList)
                } else {
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observe deletion response
        viewModel.post.observe(this) { response ->
            if (response != null && response.status == true) {
                // Refresh data after deletion
                viewModel.getDataDiri() // Fetch updated list
                Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show()
            }
        }

        // Button click to add data
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        // Button click to search data
        btnSearch.setOnClickListener {
            val searchText = txtSearch.text.toString().trim()
            if (searchText.isNotEmpty()) {
                // Perform search logic
                val filteredList = originalDataList.filter { item ->
                    item.name?.contains(searchText, ignoreCase = true) == true ||
                            item.address?.contains(searchText, ignoreCase = true) == true
                }
                adapter.updateData(filteredList)
            } else {
                // If search text is empty, show original data
                adapter.updateData(originalDataList)
            }
        }
    }

    private fun deleteCollect(item: CollectItem) {
        val idString = item.id?.toString() ?: run {
            //showToast("Invalid item ID")
            return // Exit the function if ID is null
        }

        val rbid = idString.toRequestBody("text/plain".toMediaTypeOrNull())
        viewModel.deleteCollect(rbid)
    }

    private fun editCollect(item: CollectItem?) {
        val intent = Intent(this, AddActivity::class.java).apply {
            putExtra("ITEM_ID", item?.id)
            putExtra("ITEM_NAME", item?.name)
            putExtra("ITEM_ADDRESS", item?.address)
            putExtra("ITEM_OUTSTANDING", item?.outstanding)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDataDiri()
    }
}
