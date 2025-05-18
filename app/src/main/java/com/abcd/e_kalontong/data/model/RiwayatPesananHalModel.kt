package com.abcd.e_kalontong.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class RiwayatPesananHalModel (

    @SerializedName("user")
    val user: UserModel? = null,

    @SerializedName("riwayat_pesanan")
    val riwayatPesanan: ArrayList<RiwayatPesananModel>? = null,

)