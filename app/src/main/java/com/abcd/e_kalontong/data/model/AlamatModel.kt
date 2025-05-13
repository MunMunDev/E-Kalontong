package com.abcd.e_kalontong.data.model

import com.google.gson.annotations.SerializedName

class AlamatModel(
    @SerializedName("id_alamat")
    val id_alamat: String? = null,

    @SerializedName("id_user")
    val idUser: String? = null,

    @SerializedName("nama_lengkap")
    val nama_lengkap: String? = null,

    @SerializedName("nomor_hp")
    val nomor_hp: String? = null,

    @SerializedName("detail_alamat")
    val detail_alamat: String? = null,

    @SerializedName("main")
    val main: String? = null,

    @SerializedName("kecamatan")
    val kecamatan: KecamatanModel? = null,
)