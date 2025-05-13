package com.abcd.e_kalontong.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.databinding.ListPembayaranPesananBinding
import com.abcd.e_kalontong.utils.KonversiRupiah

class ListPaymentAdapter(
    private val listTestimoni: ArrayList<PesananModel>,
): RecyclerView.Adapter<ListPaymentAdapter.ViewHolder>() {

    private val rupiah = KonversiRupiah()

    class ViewHolder(val binding: ListPembayaranPesananBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListPembayaranPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTestimoni.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pesanan = listTestimoni[position]
        holder.binding.apply {
            val nama = pesanan.produk!!.produk
            val jumlah = pesanan.jumlah
            val harga = rupiah.rupiah(pesanan.produk.harga!!.toLong())

            tvProduk.text = nama
            tvJumlah.text = jumlah
            tvHarga.text = harga

        }
    }
}