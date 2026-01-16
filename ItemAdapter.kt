package com.example.task4_inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val items: List<Item>,
    private val onItemSelected: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // ViewHolder for each item row
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvDetails: TextView = view.findViewById(R.id.tvItemDetails)

        init {
            view.setOnClickListener {
                val item = items[adapterPosition]
                onItemSelected(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.tvDetails.text =
            "${item.id}: ${item.name} - ${item.type} - Qty: ${item.quantity}"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
