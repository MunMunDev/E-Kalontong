package com.abcd.e_kalontong.utils

import android.view.View
import com.abcd.e_kalontong.data.model.PesananModel

interface OnClickItem {
    interface ClickPesanan{
        fun clickItemPesanan(pesanan: PesananModel, it: View)
        fun clickGambarPesanan(gambar: String, produk:String, it: View)
    }
}