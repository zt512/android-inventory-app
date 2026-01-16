package com.example.task4_inventory

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InventoryDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "inventory.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_TYPE TEXT,
                $COLUMN_QUANTITY INTEGER
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert item into the database
    fun addItem(item: Item): Boolean {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, item.name)
            put(COLUMN_TYPE, item.type)
            put(COLUMN_QUANTITY, item.quantity)
        }

        val result = db.insert(TABLE_NAME, null, values)
        db.close()

        return result != -1L
    }

    // Retrieve all items
    fun getAllItems(): List<Item> {
        val itemList = mutableListOf<Item>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val item = Item(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
                )

                itemList.add(item)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemList
    }

    // Update existing item
    fun updateItem(item: Item): Boolean {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, item.name)
            put(COLUMN_TYPE, item.type)
            put(COLUMN_QUANTITY, item.quantity)
        }

        val result = db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(item.id.toString())
        )

        db.close()

        return result > 0
    }

    // Delete item by ID
    fun deleteItem(id: Int): Boolean {
        val db = writableDatabase

        val result = db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )

        db.close()

        return result > 0
    }
}
