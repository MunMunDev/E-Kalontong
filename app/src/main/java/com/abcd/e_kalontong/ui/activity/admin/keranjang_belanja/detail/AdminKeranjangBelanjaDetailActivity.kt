package com.abcd.e_kalontong.ui.activity.admin.keranjang_belanja.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.PesananAdapter
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.databinding.ActivityAdminKeranjangBelanjaDetailBinding
import com.abcd.e_kalontong.databinding.ActivityPesananDetailBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminKeranjangBelanjaDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminKeranjangBelanjaDetailBinding
    private lateinit var pesanan : ArrayList<PesananModel>
    private var nama = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminKeranjangBelanjaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setAppNavBar()
        setButton()
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i != null){
            pesanan = i.getParcelableArrayList("pesanan")!!  // riwayat pesanan
            nama = i.getString("nama")!!

            setAdapter(pesanan)
        }
    }

    private fun setAppNavBar() {
        binding.appNavbarDrawer.apply{
            ivNavDrawer.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
            tvTitle.text = "Detail Pesanan $nama"
        }
    }

    private fun setButton() {
        binding.apply {
            appNavbarDrawer.ivBack.setOnClickListener {
                finish()
            }
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
            rvDetailRiwayatPesanan.layoutManager = LinearLayoutManager(this@AdminKeranjangBelanjaDetailActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailRiwayatPesanan.adapter = adapter
        }
    }

    private fun setShowImage(gambar: String, produk: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminKeranjangBelanjaDetailActivity)
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

        Glide.with(this@AdminKeranjangBelanjaDetailActivity)
            .load(gambar) // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }
}