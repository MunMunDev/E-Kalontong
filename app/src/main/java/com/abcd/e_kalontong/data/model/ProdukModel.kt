package com.abcd.e_kalontong.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ProdukModel (
    @SerializedName("id_produk")
    val id_produk: Int? = null,

    @SerializedName("id_jenis_produk")
    val id_jenis_produk: String? = null,

    @SerializedName("produk")
    val produk: String? = null,

    @SerializedName("harga")
    val harga: String? = null,

    @SerializedName("stok")
    val stok: Int? = null,

    @SerializedName("gambar")
    val gambar: String? = null,

    @SerializedName("jenis_produk")
    val jenis_produk: JenisProdukModel? = null,

)