package com.abcd.e_kalontong.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.databinding.ListPilihAlamatBinding
import com.abcd.e_kalontong.utils.OnClickItem

class PilihAlamatAdapter(
    private val list: ArrayList<AlamatModel>,
    private val click: OnClickItem.ClickPilihAlamat
): RecyclerView.Adapter<PilihAlamatAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListPilihAlamatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPilihAlamatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.binding.apply {
            val kecamatan = "Kacamatan ${data.kecamatan!!.kecamatan} Kabupaten Sidenreng Rappang"
            tvNama.text = data.nama_lengkap
            tvNomorHp.text = data.nomor_hp
            tvKecamatan.text = kecamatan
            tvAlamatDetail.text = data.detail_alamat

            if(data.main=="1"){
                tvTitleAlamatSekarang.visibility = View.VISIBLE
            } else{
                tvTitleAlamatSekarang.visibility = View.GONE
            }

            clAlamat.setOnClickListener {
                click.clickItemPilih(data, it)
            }
            btnUbah.setOnClickListener{
                click.clickItemEdit(data, it)
            }
        }

    }
}