package com.abcd.e_kalontong.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.databinding.ListPesananBinding
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide

class AdminRiwayatPesananDetailAdapter(
    private val list: ArrayList<RiwayatPesananModel>,
    private val click: OnClickItem.ClickAdminRiwayatPesananDetail
): RecyclerView.Adapter<AdminRiwayatPesananDetailAdapter.ViewHolder>() {
    private val rupiah = KonversiRupiah()
    class ViewHolder(val binding: ListPesananBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPesanan = list[position]

        holder.binding.apply {
            tvProduk.text = dataPesanan.produk
            tvHarga.text = rupiah.rupiah(dataPesanan.harga!!.trim().toLong())
            tvJumlah.text = "${dataPesanan.jumlah}"

            Log.d("AdminTAG", "onBindViewHolder: ${Constant.LOCATION_GAMBAR}${dataPesanan.gambar}")

            Glide.with(holder.itemView.context)
                .load("${Constant.LOCATION_GAMBAR}${dataPesanan.gambar}") // URL Gambar
                .placeholder(R.drawable.loading)
                .error(R.drawable.icon_product_home)
                .into(ivGambar) // imageView mana yang akan diterapkan

            ivGambar.setOnClickListener {
                click.clickItemGambarProduk(
                    dataPesanan.produk!!,
                    "${Constant.LOCATION_GAMBAR}${dataPesanan.gambar!!}",
                    it
                )
            }
        }
    }
}