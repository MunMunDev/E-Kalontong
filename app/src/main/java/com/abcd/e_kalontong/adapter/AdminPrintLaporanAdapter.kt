package com.abcd.e_kalontong.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.databinding.ListAdminPrintBinding
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide

class AdminPrintLaporanAdapter(
    private var listRiwayatPesanan: ArrayList<RiwayatPesananModel>
): RecyclerView.Adapter<AdminPrintLaporanAdapter.ViewHolder>() {
    private var konversiRupiah: KonversiRupiah = KonversiRupiah()

    class ViewHolder(val binding: ListAdminPrintBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminPrintBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listRiwayatPesanan.size+1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvNama.text = "Nama"
                tvProduk.text = "Produk"
                tvHarga.text = "Harga"
                tvJumlah.text = "Jumlah"
                tvMetodePembayaran.text = "Pembayaran"

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvNama.setBackgroundResource(R.drawable.bg_table_title)
                tvProduk.setBackgroundResource(R.drawable.bg_table_title)
                tvHarga.setBackgroundResource(R.drawable.bg_table_title)
                tvJumlah.setBackgroundResource(R.drawable.bg_table_title)
                tvMetodePembayaran.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvNama.setTextColor(Color.parseColor("#ffffff"))
                tvProduk.setTextColor(Color.parseColor("#ffffff"))
                tvHarga.setTextColor(Color.parseColor("#ffffff"))
                tvJumlah.setTextColor(Color.parseColor("#ffffff"))
                tvMetodePembayaran.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvNama.setTypeface(null, Typeface.BOLD)
                tvProduk.setTypeface(null, Typeface.BOLD)
                tvHarga.setTypeface(null, Typeface.BOLD)
                tvJumlah.setTypeface(null, Typeface.BOLD)
                tvMetodePembayaran.setTypeface(null, Typeface.BOLD)
            }
            else{
                val riwayatPesanan = listRiwayatPesanan[(position-1)]
                val nama = riwayatPesanan.nama_lengkap
                val produk = riwayatPesanan.produk
                val harga = riwayatPesanan.harga
                val jumlah = riwayatPesanan.jumlah
                val metode_pembayaran = riwayatPesanan.metode_pembayaran

                tvNo.text = "$position"
                tvNama.text = nama
                tvProduk.text = produk
                tvHarga.text = konversiRupiah.rupiah(harga!!.toLong())
                tvJumlah.text = jumlah.toString()
                tvMetodePembayaran.text = metode_pembayaran

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvNama.setBackgroundResource(R.drawable.bg_table)
                tvProduk.setBackgroundResource(R.drawable.bg_table)
                tvHarga.setBackgroundResource(R.drawable.bg_table)
                tvJumlah.setBackgroundResource(R.drawable.bg_table)
                tvMetodePembayaran.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvNama.setTextColor(Color.parseColor("#000000"))
                tvProduk.setTextColor(Color.parseColor("#000000"))
                tvHarga.setTextColor(Color.parseColor("#000000"))
                tvJumlah.setTextColor(Color.parseColor("#000000"))
                tvMetodePembayaran.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvNama.setTypeface(null, Typeface.NORMAL)
                tvProduk.setTypeface(null, Typeface.NORMAL)
                tvHarga.setTypeface(null, Typeface.NORMAL)
                tvJumlah.setTypeface(null, Typeface.NORMAL)
                tvMetodePembayaran.setTypeface(null, Typeface.NORMAL)

            }
        }
    }
}