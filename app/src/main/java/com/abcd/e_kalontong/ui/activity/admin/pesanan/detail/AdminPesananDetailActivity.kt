package com.abcd.e_kalontong.ui.activity.admin.pesanan.detail

import android.os.Bundle
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
import com.abcd.e_kalontong.adapter.AdminRiwayatPesananDetailAdapter
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.databinding.ActivityAdminPesananDetailBinding
import com.abcd.e_kalontong.databinding.ActivityAdminRiwayatPesananDetailBinding
import com.abcd.e_kalontong.databinding.AlertDialogKeteranganBinding
import com.abcd.e_kalontong.databinding.AlertDialogKonfirmasiBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.network.UIState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminPesananDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPesananDetailBinding
    private lateinit var riwayatPesanan : ArrayList<RiwayatPesananModel>
    @Inject
    lateinit var loading: LoadingAlertDialog
    private val viewModel: AdminPesananDetailViewModel by viewModels()
    private var nama : String = ""
    private var bayar : String = ""
    private var kirim : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPesananDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setAppNavBar()
        setButton()
        getBayar()
        getDikirim()
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            riwayatPesanan = i.getParcelableArrayList("pesanan")!!  // riwayat pesanan
            nama = i.getString("nama")!!

            bayar = riwayatPesanan[0].bayar!!
            kirim = riwayatPesanan[0].selesai!!
            cekKirim(kirim)     // cek sudah kirim
            cekBayar(bayar)     // cek sudah bayar

            setAdapter(riwayatPesanan)
            setDataAlamat(riwayatPesanan)
        }
    }

    private fun cekKirim(kirim: String){
        if(kirim == "1"){
            binding.tvKirim.text = "Sudah Dikirim"
        } else{
            binding.tvKirim.text = "Belum Dikirim"
            binding.tvKeterangan.text = "Konfirmasi Selesai Pengiriman"
        }
    }

    private fun cekBayar(bayar: String){
        if(bayar == "1"){
            binding.tvBayar.text = "Sudah Bayar"
        } else{
            binding.tvBayar.text = "Belum Bayar"
            binding.tvKeterangan.text = "Konfirmasi Selesai Pembayaran"
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
            tvKeterangan.setOnClickListener {
                setShowKeterangan()
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
            rvDetailRiwayatPesanan.layoutManager = LinearLayoutManager(this@AdminPesananDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailRiwayatPesanan.adapter = adapter
        }
    }

    private fun setShowImage(produk: String, gambar: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPesananDetailActivity)
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

        Glide.with(this@AdminPesananDetailActivity)
            .load(gambar) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan
    }

    private fun setShowKeterangan(){
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPesananDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            if(bayar == "0"){
                tvTitleKonfirmasi.text = "Konfirmasi Pembayaran"
                tvBodyKonfirmasi.text = "Tekan Tombol Konfirmasi Untuk Konfirmasi Pembayaran User"
            } else if(kirim == "0"){
                tvTitleKonfirmasi.text = "Konfirmasi Pengiriman"
                tvBodyKonfirmasi.text = "Tekan Tombol Konfirmasi Untuk Konfirmasi Telah Melakukan Pengiriman"
            }

            btnKonfirmasi.setOnClickListener {
                val idPemesanan = riwayatPesanan[0].id_pemesanan!!
                if(bayar == "0"){
                    postBayar(idPemesanan)
                } else if(kirim == "0"){
                    postDikirim(idPemesanan)
                }

                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postBayar(idPemesanan: String){
        viewModel.postBayar(idPemesanan)
    }

    private fun getBayar(){
        viewModel.getBayar().observe(this@AdminPesananDetailActivity){result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@AdminPesananDetailActivity)
                is UIState.Success -> setSuccessPostBayar(result.data)
                is UIState.Failure -> setFailurePostBayar(result.message)
            }
        }
    }

    private fun setSuccessPostBayar(data: ResponseModel) {
        if(data.status == "0"){
            Toast.makeText(this@AdminPesananDetailActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminPesananDetailActivity, "${data.message_response}", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setFailurePostBayar(message: String) {
        Toast.makeText(this@AdminPesananDetailActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }


    private fun postDikirim(idPemesanan: String){
        viewModel.postDikirim(idPemesanan)
    }

    private fun getDikirim(){
        viewModel.getDikirim().observe(this@AdminPesananDetailActivity){result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@AdminPesananDetailActivity)
                is UIState.Success -> setSuccessPostDikirim(result.data)
                is UIState.Failure -> setFailurePostDikirim(result.message)
            }
        }
    }

    private fun setSuccessPostDikirim(data: ResponseModel) {
        if(data.status == "0"){
            Toast.makeText(this@AdminPesananDetailActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminPesananDetailActivity, "${data.message_response}", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setFailurePostDikirim(message: String) {
        Toast.makeText(this@AdminPesananDetailActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }


}