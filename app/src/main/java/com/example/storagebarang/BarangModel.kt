package com.example.storagebarang

import java.util.*

data class BarangModel(
    var id: Int = getAutoId(),
    var name: String = "",
    var harga: String = ""
        ){
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}