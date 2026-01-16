// MainActivity.kt (clean showcase version)
package com.example.task4_inventory

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: InventoryDBHelper
    private lateinit var adapter: ItemAdapter
    private var itemList = mutableListOf<Item>()
    private var selectedItemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = InventoryDBHelper(this)

        val etName = findViewById<EditText>(R.id.etItemName)
        val spinnerType = findViewById<Spinner>(R.id.spinnerItemType)
        val spinnerQty = findViewById<Spinner>(R.id.spinnerQuantity)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fun refreshList() {
            itemList = dbHelper.getAllItems().toMutableList()
            adapter = ItemAdapter(itemList) { item ->
                selectedItemId = item.id
                etName.setText(item.name)
            }
            recyclerView.adapter = adapter
        }

        btnAdd.setOnClickListener {
            val item = Item(
                id = 0,
                name = etName.text.toString(),
                type = spinnerType.selectedItem.toString(),
                quantity = spinnerQty.selectedItem.toString().toInt()
            )

            dbHelper.addItem(item)
            refreshList()
        }

        btnUpdate.setOnClickListener {
            selectedItemId?.let {
                val item = Item(
                    id = it,
                    name = etName.text.toString(),
                    type = spinnerType.selectedItem.toString(),
                    quantity = spinnerQty.selectedItem.toString().toInt()
                )

                dbHelper.updateItem(item)
                refreshList()
            }
        }

        btnDelete.setOnClickListener {
            selectedItemId?.let {
                dbHelper.deleteItem(it)
                refreshList()
            }
        }

        refreshList()
    }
}
