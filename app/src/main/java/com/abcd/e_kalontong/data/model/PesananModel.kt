package com.abcd.e_kalontong.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PesananModel (
    @SerializedName("id_pesanan")
    val id_pesanan: Int? = null,

    @SerializedName("id_user")
    val idUser: String? = null,

    @SerializedName("id_produk")
    val id_produk: String? = null,

    @SerializedName("jumlah")
    val jumlah: String? = null,

    @SerializedName("waktu")
    val waktu: String? = null,

    @SerializedName("produk")
    val produk: ProdukModel? = null,

)