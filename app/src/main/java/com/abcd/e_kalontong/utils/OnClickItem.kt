package com.abcd.e_kalontong.utils

import android.view.View
import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel

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
}