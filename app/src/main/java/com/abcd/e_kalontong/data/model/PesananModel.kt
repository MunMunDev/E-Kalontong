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

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ProdukModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id_pesanan)
        parcel.writeString(idUser)
        parcel.writeString(id_produk)
        parcel.writeString(jumlah)
        parcel.writeString(waktu)
        parcel.writeParcelable(produk, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PesananModel> {
        override fun createFromParcel(parcel: Parcel): PesananModel {
            return PesananModel(parcel)
        }

        override fun newArray(size: Int): Array<PesananModel?> {
            return arrayOfNulls(size)
        }
    }
}