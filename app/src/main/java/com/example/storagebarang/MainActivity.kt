package com.example.storagebarang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var nama_barang: EditText
    private lateinit var harga_barang: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button

    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: BarangAdapter? = null

    private var std: BarangModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener{ addBarang() }
        btnView.setOnClickListener{ getBarang() }
        btnUpdate.setOnClickListener { updateBarang() }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()

            //update
            nama_barang.setText(it.name)
            harga_barang.setText(it.harga)
            std = it
        }

        adapter?.setOnClickDeleteItem {
            deleteBarang(it.id)
        }
    }

    private fun getBarang() {
        val stdList = sqliteHelper.getAllBarang()
        Log.e("ppppp", "${stdList.size}")

        //ok
        adapter?.addItems(stdList)
    }

    private fun addBarang() {
        val name = nama_barang.text.toString()
        val harga = harga_barang.text.toString()

        if (name.isEmpty() || harga.isEmpty()) {
            Toast.makeText(this, "Isi kolom formulir!", Toast.LENGTH_SHORT).show()
        }else{
            val std = BarangModel(name = name, harga = harga)
            val status = sqliteHelper.insertBarang(std)

            //cek sukse opo gak
            if (status > -1) {
                Toast.makeText(this, "Barang Ditambahkan", Toast.LENGTH_SHORT).show()
                clearEditText()
                getBarang()
            }else {
                Toast.makeText(this, "Data gagal Disimpan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateBarang() {
        val name = nama_barang.text.toString()
        val harga = harga_barang.text.toString()

        //cek
        if(name == std?.name) {
            Toast.makeText(this,"Data tidak dirubah!", Toast.LENGTH_SHORT).show()
            return
        }

        if (std == null) return

        val std = BarangModel(id = std!!.id, name = name)
        val status = sqliteHelper.updateBarang(std)

        if (status > -1) {
            clearEditText()
            getBarang()
        } else {
            Toast.makeText(this, "Update Gagal!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deleteBarang(id:Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Apakah anda ingin menghapus data ini ?")
        builder.setCancelable(true)

        builder.setPositiveButton("Yes") { dialog, _ ->
            sqliteHelper.deleteBarangById(id)
            getBarang()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }
    private fun clearEditText() {
        nama_barang.setText("")
        harga_barang.setText("")
        nama_barang.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BarangAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        nama_barang = findViewById(R.id.nama_barang)
        harga_barang = findViewById(R.id.harga_barang)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}