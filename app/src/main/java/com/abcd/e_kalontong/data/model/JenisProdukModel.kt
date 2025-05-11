package com.abcd.e_kalontong.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class JenisProdukModel (
    @SerializedName("id_jenis_produk")
    val id_jenis_produk: Int? = null,

    @SerializedName("jenis_produk")
    val jenis_produk: String? = null,

)