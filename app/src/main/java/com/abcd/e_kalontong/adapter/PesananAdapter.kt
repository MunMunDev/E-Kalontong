package com.abcd.e_kalontong.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.databinding.ListPesananBinding
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide

class PesananAdapter(
    private val list: ArrayList<PesananModel>,
    private val click: OnClickItem.ClickPesanan
): RecyclerView.Adapter<PesananAdapter.ViewHolder>() {
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
            tvProduk.text = dataPesanan.produk!!.produk!!
            tvHarga.text = rupiah.rupiah(dataPesanan.produk.harga!!.trim().toLong())
            tvJumlah.text = "${dataPesanan.jumlah}"

            ivGambar.setOnClickListener {
                click.clickGambarPesanan(
                    "${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${dataPesanan.produk.gambar!!}",
                    dataPesanan.produk.produk!!,
                    it
                )
            }

            Glide.with(holder.itemView)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${dataPesanan.produk.gambar}")
                .placeholder(R.drawable.loading)
                .error(R.drawable.gambar_not_have_image)
                .into(ivGambar)
        }
        holder.itemView.setOnClickListener{
            click.clickItemPesanan(dataPesanan, it)
        }
    }
}