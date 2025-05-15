package com.abcd.e_kalontong.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class RiwayatPesananModel (
    @SerializedName("id_riwayat_pesanan")
    val id_riwayat_pesanan: Int? = null,

    @SerializedName("id_pemesanan")
    val id_pemesanan: String? = null,

    @SerializedName("id_user")
    val idUser: String? = null,

    @SerializedName("produk")
    val produk: String? = null,

    @SerializedName("harga")
    var harga: String? = null,

    @SerializedName("jumlah")
    var jumlah: String? = null,

    @SerializedName("gambar")
    val gambar: String? = null,

    @SerializedName("nama_lengkap")
    val nama_lengkap: String? = null,

    @SerializedName("nomor_hp")
    val nomor_hp: String? = null,

    @SerializedName("kecamatan")
    val kecamatan: String? = null,

    @SerializedName("detail_alamat")
    val detail_alamat: String? = null,

    @SerializedName("metode_pembayaran")
    val metode_pembayaran: String? = null,

    @SerializedName("bayar")
    val bayar: String? = null,

    @SerializedName("selesai")
    val selesai: String? = null,

    @SerializedName("waktu")
    val waktu: String? = null,

    @SerializedName("jum_jenis_produk")
    var jum_jenis_produk: String? = null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id_riwayat_pesanan)
        parcel.writeString(id_pemesanan)
        parcel.writeString(idUser)
        parcel.writeString(produk)
        parcel.writeString(harga)
        parcel.writeString(jumlah)
        parcel.writeString(gambar)
        parcel.writeString(nama_lengkap)
        parcel.writeString(nomor_hp)
        parcel.writeString(kecamatan)
        parcel.writeString(detail_alamat)
        parcel.writeString(metode_pembayaran)
        parcel.writeString(bayar)
        parcel.writeString(selesai)
        parcel.writeString(waktu)
        parcel.writeString(jum_jenis_produk)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RiwayatPesananModel> {
        override fun createFromParcel(parcel: Parcel): RiwayatPesananModel {
            return RiwayatPesananModel(parcel)
        }

        override fun newArray(size: Int): Array<RiwayatPesananModel?> {
            return arrayOfNulls(size)
        }
    }
}