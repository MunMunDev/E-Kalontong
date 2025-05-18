package com.abcd.e_kalontong.ui.activity.admin.riwayat_pesanan.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.AdminRiwayatPesananDetailAdapter
import com.abcd.e_kalontong.adapter.PesananAdapter
import com.abcd.e_kalontong.data.model.JenisProdukModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.data.model.UserModel
import com.abcd.e_kalontong.databinding.ActivityAdminRiwayatPesananDetailBinding
import com.abcd.e_kalontong.databinding.ActivityPesananDetailBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide

class AdminRiwayatPesananDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminRiwayatPesananDetailBinding
    private lateinit var riwayatPesanan : ArrayList<RiwayatPesananModel>
    private var nama : String = ""
    private var bayar = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRiwayatPesananDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setAppNavBar()
        setButton()
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            riwayatPesanan = i.getParcelableArrayList("pesanan")!!  // riwayat pesanan
            nama = i.getString("nama")!!

            bayar = riwayatPesanan[0].bayar!!   // cek sudah bayar
            if(bayar == "0"){
                binding.tvKeterangan.text = "Belum Bayar"
            } else{
                binding.tvKeterangan.text = "Sudah Bayar"
            }

            setAdapter(riwayatPesanan)
            setDataAlamat(riwayatPesanan)
        }
    }

    private fun setAppNavBar() {
        binding.appNavbarDrawer.apply{
            ivNavDrawer.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
            tvTitle.text = "Detail Riwayat Pesanan $nama"
        }
    }

    private fun setButton() {
        binding.apply {
            appNavbarDrawer.ivBack.setOnClickListener {
                finish()
            }
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

    private fun setAdapter(data: ArrayList<RiwayatPesananModel>) {
        val adapter = AdminRiwayatPesananDetailAdapter(data, object : OnClickItem.ClickAdminRiwayatPesananDetail{
            override fun clickItemGambarProduk(keterangan: String, gambar: String, it: View) {
                setShowImage(keterangan, gambar)
            }
        })
        binding.apply {
            rvDetailRiwayatPesanan.layoutManager = LinearLayoutManager(this@AdminRiwayatPesananDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailRiwayatPesanan.adapter = adapter
        }
    }

    private fun setShowImage(produk: String, gambar: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananDetailActivity)
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

        Glide.with(this@AdminRiwayatPesananDetailActivity)
            .load(gambar) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }
}