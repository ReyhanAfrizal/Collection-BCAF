package com.reyhan.collect.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reyhan.collect.R
import com.reyhan.collect.model.CollectItem

class CollectionAdapter(
    private var dataList: MutableList<CollectItem?>,
    private val onDeleteClick: (CollectItem) -> Unit,
    private val onEditClick: (CollectItem?) -> Unit
) : RecyclerView.Adapter<CollectionAdapter.DataViewHolder>() {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtNama: TextView = itemView.findViewById(R.id.txtNamaItem)
        var txtAlamat: TextView = itemView.findViewById(R.id.txtAlamatItem)
        var txtOutstanding: TextView = itemView.findViewById(R.id.txtOutstandingItem)
        var btnHapus: Button = itemView.findViewById(R.id.btnHapus)
        var btnEdit: Button = itemView.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]

        if (data != null) {
            holder.txtNama.text = data.name ?: "N/A" // Use a default value if name is null
            holder.txtAlamat.text = data.address ?: "N/A" // Use a default value if address is null
            holder.txtOutstanding.text = data.outstanding ?: "0" // Use a default value if outstanding is null

            // Set click listener for delete button
            holder.btnHapus.setOnClickListener {
                showDeleteConfirmationDialog(holder.itemView.context, data)
            }

            // Set click listener for edit button
            holder.btnEdit.setOnClickListener {
                onEditClick(data)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun updateData(newData: List<CollectItem?>?) {
        dataList.clear()
        if (newData != null) {
            dataList.addAll(newData.filterNotNull())
        }
        notifyDataSetChanged()
    }

    fun filterData(query: String) {
        val filteredData = dataList.filter {
            it?.name?.contains(query, ignoreCase = true) == true ||
                    it?.address?.contains(query, ignoreCase = true) == true
        }
        updateData(filteredData)
    }

    private fun showDeleteConfirmationDialog(context: Context, item: CollectItem) {
        AlertDialog.Builder(context)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus item ini?")
            .setPositiveButton("Hapus") { _, _ ->
                // Call the deletion service here using the item ID
                onDeleteClick(item)  // Make sure this is connected to your delete service
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
