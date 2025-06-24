package com.abcd.e_kalontong.utils

import android.view.View
import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.data.model.JenisProdukModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.data.model.UserModel

interface OnClickItem {
    interface ClickPesanan{
        fun clickItemPesanan(pesanan: PesananModel, it: View)
        fun clickGambarPesanan(gambar: String, produk:String, it: View)
    }

    interface ClickProduk{
        fun clickItemPesan(produk: ProdukModel, it: View)
    }

    interface ClickPilihAlamat{
        fun clickItemPilih(alamat: AlamatModel, it: View)
        fun clickItemEdit(alamat: AlamatModel, it: View)
    }

    interface ClickRiwayatPesanan{
        fun clickItemRiwayatPesanan(riwayatPesanan: RiwayatPesananModel, it: View)
    }

    interface ClickAdminJenisProduk{
        fun clickItemSetting(jenisProduk: JenisProdukModel, it: View)
    }

    interface ClickAdminProduk{
        fun clickItemJenisProduk(keterangan: String, jenisProduk: String, it: View)
        fun clickItemProduk(keterangan: String, produk: String, it: View)
        fun clickItemHargaProduk(keterangan: String, harga: String, it: View)
        fun clickItemGambarProduk(keterangan: String, gambar: String, it: View)
        fun clickItemSetting(produk: ProdukModel, it: View)
    }

    interface ClickAdminKeranjangBelanja{
        fun clickItem(pesanan: ArrayList<PesananModel>, nama:String, it: View)
    }

    interface ClickAkun{
        fun clickItemNama(keterangan: String, nama:String, it: View)
        fun clickItemNomoHp(keterangan: String, nomorHp:String, it: View)
        fun clickItemUsername(keterangan: String, username:String, it: View)
        fun clickItemSetting(akun: UserModel, it: View)
    }

    interface ClickAdminRiwayatPesanan{
        fun clickItem(pesanan: ArrayList<RiwayatPesananModel>, nama:String, it: View)
    }

    interface ClickAdminRiwayatPesananDetail{
        fun clickItemGambarProduk(keterangan: String, gambar: String, it: View)
    }
}