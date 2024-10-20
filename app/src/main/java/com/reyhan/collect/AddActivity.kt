package com.reyhan.collect

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.reyhan.collect.viewmodel.CollectionViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AddActivity : AppCompatActivity() {
    private lateinit var txtInputNama: EditText
    private lateinit var txtInputAlamat: EditText
    private lateinit var txtInputOutstanding: EditText
    private lateinit var btnSendData: Button

    private val viewModel: CollectionViewModel by viewModels()
    private var itemId: Int? = null // Variable to hold item ID for editing

    // Initialize components
    private fun initComponent() {
        txtInputNama = findViewById(R.id.txtNama)
        txtInputAlamat = findViewById(R.id.txtAlamat)
        txtInputOutstanding = findViewById(R.id.txtOutstanding)
        btnSendData = findViewById(R.id.btnKirimData)

        btnSendData.setOnClickListener { sendData() }

        viewModel.post.observe(this) { response ->
            if (response?.status == true) {
                Toast.makeText(this, "Data sent successfully", Toast.LENGTH_SHORT).show()
                finish() // Close activity on success
            } else {
                Toast.makeText(this, "Failed to send data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendData() {
        val nama = txtInputNama.text.toString().trim()
        val alamat = txtInputAlamat.text.toString().trim()
        val outstanding = txtInputOutstanding.text.toString().trim()

        // Validate input fields
        if (nama.isNotEmpty() && alamat.isNotEmpty() && outstanding.isNotEmpty()) {
            val rbNama = nama.toRequestBody("text/plain".toMediaTypeOrNull())
            val rbAlamat = alamat.toRequestBody("text/plain".toMediaTypeOrNull())
            val rbOutstanding = outstanding.toRequestBody("text/plain".toMediaTypeOrNull())

            // Call the appropriate method in your ViewModel based on whether we are updating or creating
            if (itemId != null) {
                viewModel.updateCollect(itemId!!.toString(), rbNama, rbAlamat, rbOutstanding)
            } else {
                viewModel.postDataDiri(rbNama, rbAlamat, rbOutstanding)
            }
        } else {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initComponent()

        // Adjust the window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check if we are editing an existing item
        itemId = intent.getStringExtra("ITEM_ID")?.toIntOrNull() // Convert to Int safely
        itemId?.let { id ->
            txtInputNama.setText(intent.getStringExtra("ITEM_NAME"))
            txtInputAlamat.setText(intent.getStringExtra("ITEM_ADDRESS"))
            txtInputOutstanding.setText(intent.getStringExtra("ITEM_OUTSTANDING"))
            btnSendData.text = "Update" // Change button text to indicate updating
        }
    }
}
