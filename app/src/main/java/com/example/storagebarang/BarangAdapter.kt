package com.example.storagebarang

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BarangAdapter : RecyclerView.Adapter<BarangAdapter.BarangViewHolder>() {
    private var stdList: ArrayList<BarangModel> = ArrayList()
    private var onClickItem: ((BarangModel) -> Unit)? = null
    private var onClickDeleteItem: ((BarangModel) -> Unit)? = null

    fun addItems(items: ArrayList<BarangModel>) {
        this.stdList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (BarangModel) -> Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (BarangModel) -> Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BarangViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.card_item_std, parent, false)
    )

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(std) }
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(std) }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class BarangViewHolder(var view: View): RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var harga = view.findViewById<TextView>(R.id.tvHarga)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(std: BarangModel) {
            id.text = std.id.toString()
            name.text = std.name
            harga.text = std.harga
        }

    }
}