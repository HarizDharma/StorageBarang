package com.example.storagebarang

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "databarang.db"
        private const val TBL_BARANG = "tbl_barang"
        private const val ID = "id"
        private const val NAME = "nama"
        private const val HARGA = "harga"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblBarang = ("CREATE TABLE " + TBL_BARANG + "("
                + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + HARGA + " TEXT" + ")")

        db?.execSQL(createTblBarang)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_BARANG")
        onCreate(db)
    }

    fun insertBarang(std: BarangModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(HARGA, std.harga)

        val success = db.insert(TBL_BARANG, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllBarang(): ArrayList<BarangModel>{
        val stdList: ArrayList<BarangModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_BARANG"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var harga: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                harga = cursor.getString(cursor.getColumnIndex("harga"))

                val std = BarangModel(id=id, name = name, harga = harga)
                stdList.add(std)
            } while (cursor.moveToNext())
        }

        return stdList
    }

    fun updateBarang(std: BarangModel): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(HARGA, std.harga)

        val success = db.update(TBL_BARANG, contentValues, "id=" + std.id, null)
        db.close()
        return success

    }

    fun deleteBarangById(id:Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TBL_BARANG, "id=$id", null)
        db.close()
        return success
    }

}