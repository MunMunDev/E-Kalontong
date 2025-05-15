package com.abcd.e_kalontong.ui.activity.pesanan.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.PesananAdapter
import com.abcd.e_kalontong.adapter.RiwayatPesananAdapter
import com.abcd.e_kalontong.data.model.JenisProdukModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.databinding.ActivityPesananDetailBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide

class PesananDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananDetailBinding
    private lateinit var riwayatPesanan : ArrayList<RiwayatPesananModel>
    private lateinit var pesanan : ArrayList<PesananModel>
    private var bayar = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppNavBar()
        setButton()
        setDataSebelumnya()
    }

    private fun setAppNavBar() {
        binding.appNavbarDrawer.apply{
            ivNavDrawer.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
            tvTitle.text = "Detail Pesanan"
        }
    }

    private fun setButton() {
        binding.apply {
            appNavbarDrawer.ivBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            riwayatPesanan = i.getParcelableArrayList("pesanan")!!  // riwayat pesanan
            pesanan = arrayListOf()
            for(value in riwayatPesanan){
                val jenisProduk = JenisProdukModel(0, "")
                val produk = ProdukModel(
                    0, "0", value.produk, value.harga, 0, value.gambar, jenisProduk
                )
                pesanan.add(
                    PesananModel(
                        0, value.idUser, "0", value.jumlah, value.waktu, produk
                    )
                )
            }

            bayar = riwayatPesanan[0].bayar!!   // cek sudah bayar
            if(bayar == "0"){
                binding.tvKeterangan.text = "Belum Bayar"
            } else{
                binding.tvKeterangan.text = "Sudah Bayar"
            }

            setAdapter(pesanan)
            setDataAlamat(riwayatPesanan)
        }
    }


    private fun setDataAlamat(data: ArrayList<RiwayatPesananModel>) {
        binding.apply {
            tvNama.text = data[0].nama_lengkap
            tvNomorHp.text = data[0].nomor_hp
            tvKecamatan.text = "Kecamatan ${data[0].kecamatan}, Kabupaten Sidenreng Rappang"
            tvAlamatDetail.text = data[0].detail_alamat
        }
    }

    private fun setAdapter(data: ArrayList<PesananModel>) {
        val adapter = PesananAdapter(data, object : OnClickItem.ClickPesanan{
            override fun clickItemPesanan(pesanan: PesananModel, it: View) {

            }

            override fun clickGambarPesanan(gambar: String, produk: String, it: View) {
                setShowImage(gambar, produk)
            }

        })
        binding.apply {
            rvDetailRiwayatPesanan.layoutManager = LinearLayoutManager(this@PesananDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailRiwayatPesanan.adapter = adapter
        }
    }

    private fun setShowImage(gambar: String, produk: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@PesananDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = produk
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@PesananDetailActivity)
            .load(gambar) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }
}