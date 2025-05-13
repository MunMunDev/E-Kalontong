package com.abcd.e_kalontong.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class JenisProdukModel (
    @SerializedName("id_jenis_produk")
    val id_jenis_produk: Int? = null,

    @SerializedName("jenis_produk")
    val jenis_produk: String? = null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id_jenis_produk)
        parcel.writeString(jenis_produk)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JenisProdukModel> {
        override fun createFromParcel(parcel: Parcel): JenisProdukModel {
            return JenisProdukModel(parcel)
        }

        override fun newArray(size: Int): Array<JenisProdukModel?> {
            return arrayOfNulls(size)
        }
    }
}