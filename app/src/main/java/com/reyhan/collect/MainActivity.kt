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

class MainActivity : AppCompatActivity() {
    private lateinit var btnAdd: Button
    private lateinit var btnSearch: Button
    private lateinit var txtSearch: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CollectionAdapter

    private val viewModel: CollectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        btnAdd = findViewById(R.id.btnTambahData)
        btnSearch = findViewById(R.id.btnSearch) // Ensure button ID matches your layout
        txtSearch = findViewById(R.id.txtSearch) // Ensure EditText ID matches your layout
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
                    adapter.updateData(response.data.collect)
                } else {
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
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
                // Perform search logic here (if needed)
                Toast.makeText(this, "Searching for: $searchText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteCollect(item: CollectItem) {
        viewModel.deleteCollect((item.id?.toInt() ?: 0).toString()) // Ensure to convert to Int if ID is not null
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
}
