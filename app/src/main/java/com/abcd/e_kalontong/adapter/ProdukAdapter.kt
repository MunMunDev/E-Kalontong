package com.abcd.e_kalontong.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.aplikasipenjualanplafon.utils.TanggalDanWaktu
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.databinding.ListProdukBinding
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide
import java.util.Locale

class ProdukAdapter(
    private var listProduk: ArrayList<ProdukModel>,
    private var click: OnClickItem.ClickProduk
): RecyclerView.Adapter<ProdukAdapter.ViewHolder>() {

    private val rupiah = KonversiRupiah()
    private val tanggalDanWaktu = TanggalDanWaktu()
    private var tempProduk = listProduk

    @SuppressLint("NotifyDataSetChanged", "DefaultLocale")
    fun searchData(kata: String){
        val vKata = kata.lowercase(Locale.getDefault()).trim()
        val data = listProduk.filter {
            (
                it.produk!!.lowercase().trim().contains(vKata)
                or
                it.jenis_produk!!.jenis_produk!!.lowercase().trim().contains(vKata)
            )
        } as ArrayList
        Log.d("DetailTAG", "searchData: ${data.size}")
        tempProduk = data
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListProdukBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tempProduk.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produk = tempProduk[position]
        holder.apply {
            binding.apply {
                Glide.with(itemView.context)
                    .load("${Constant.LOCATION_GAMBAR}${produk.gambar}") // URL Gambar
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.icon_product_home)
                    .into(ivGambarProduk) // imageView mana yang akan diterapkan

                tvJudulProduk.text = produk.produk
                val harga = rupiah.rupiah(produk.harga!!.toLong())
                tvHarga.text = harga

                itemView.setOnClickListener {
                    click.clickItemPesan(produk, it)
                }

            }
        }
    }
}