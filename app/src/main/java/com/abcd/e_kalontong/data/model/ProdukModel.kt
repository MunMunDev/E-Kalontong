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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readParcelable(JenisProdukModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id_produk)
        parcel.writeString(id_jenis_produk)
        parcel.writeString(produk)
        parcel.writeString(harga)
        parcel.writeValue(stok)
        parcel.writeString(gambar)
        parcel.writeParcelable(jenis_produk, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProdukModel> {
        override fun createFromParcel(parcel: Parcel): ProdukModel {
            return ProdukModel(parcel)
        }

        override fun newArray(size: Int): Array<ProdukModel?> {
            return arrayOfNulls(size)
        }
    }
}