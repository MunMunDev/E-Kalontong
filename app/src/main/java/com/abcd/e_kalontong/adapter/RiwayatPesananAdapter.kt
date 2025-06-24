package com.abcd.e_kalontong.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.aplikasipenjualanplafon.utils.TanggalDanWaktu
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.databinding.ListRiwayatPesananBinding
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.OnClickItem

class RiwayatPesananAdapter(
    private val list: ArrayList<RiwayatPesananModel>,
    private val click: OnClickItem.ClickRiwayatPesanan
): RecyclerView.Adapter<RiwayatPesananAdapter.ViewHolder>() {
    private val rupiah = KonversiRupiah()
    private val tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListRiwayatPesananBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListRiwayatPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPesanan = list[position]

        holder.binding.apply {
            tvJumlahJenisProduk.text = dataPesanan.jum_jenis_produk
            tvTotalHargaProduk.text = rupiah.rupiah(dataPesanan.harga!!.trim().toLong())
            tvTanggalWaktu.text = tanggalDanWaktu.konversiTanggalDanWaktu(dataPesanan.waktu!!)
        }
        holder.itemView.setOnClickListener{
            click.clickItemRiwayatPesanan(dataPesanan, it)
        }
    }
}