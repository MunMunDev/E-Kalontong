package com.abcd.e_kalontong.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PesananHalModel (
    @SerializedName("user")
    val user: UserModel? = null,

    @SerializedName("pesanan")
    val pesanan: ArrayList<PesananModel>? = null,

)