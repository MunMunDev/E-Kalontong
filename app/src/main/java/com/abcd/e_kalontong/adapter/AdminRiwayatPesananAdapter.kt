package com.abcd.e_kalontong.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.aplikasipenjualanplafon.utils.TanggalDanWaktu
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.RiwayatPesananHalModel
import com.abcd.e_kalontong.data.model.UserModel
import com.abcd.e_kalontong.databinding.ListAdminKeranjangBelanjaBinding
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide

class AdminRiwayatPesananAdapter(
    private var arrayListPesananHal: ArrayList<RiwayatPesananHalModel>,
    private var onClickItem: OnClickItem.ClickAdminRiwayatPesanan
): RecyclerView.Adapter<AdminRiwayatPesananAdapter.ViewHolder>() {
    private var konversiRupiah: KonversiRupiah = KonversiRupiah()
    private var tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListAdminKeranjangBelanjaBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminKeranjangBelanjaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (arrayListPesananHal.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val listPesanan = arrayListPesananHal[position]

            val jumlahJenisProduk = listPesanan.riwayatPesanan!!.size
            val totalHarga = listPesanan.riwayatPesanan.sumOf { (it.harga!!.toInt() * it.jumlah!!.toInt()) }

            // Tanggal dan waktu
            val arrayTanggalDanWaktu = listPesanan.riwayatPesanan[0].waktu!!.split(" ")
            val tanggal = tanggalDanWaktu.konversiBulan(arrayTanggalDanWaktu[0])
            val waktu = tanggalDanWaktu.waktuNoSecond(arrayTanggalDanWaktu[1])
            val valueTanggalDanWaktu = "$tanggal $waktu"

            tvNama.text = listPesanan.user!!.nama
            tvJumlahJenisProduk.text = "$jumlahJenisProduk Jenis Produk"
            tvHarga.text = konversiRupiah.rupiah(totalHarga.toLong())
            tvTanggal.text = valueTanggalDanWaktu

            holder.itemView.setOnClickListener{
                onClickItem.clickItem(listPesanan.riwayatPesanan, listPesanan.user.nama!!, it)
            }

        }
    }
}