package com.abcd.e_kalontong.ui.activity.admin.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abcd.e_kalontong.databinding.ActivityAdminMainBinding
import com.abcd.e_kalontong.ui.activity.admin.akun.AdminAkunActivity
import com.abcd.e_kalontong.ui.activity.admin.jenis_produk.AdminJenisProdukActivity
import com.abcd.e_kalontong.ui.activity.admin.keranjang_belanja.AdminKeranjangBelanjaActivity
import com.abcd.e_kalontong.ui.activity.admin.produk.AdminProdukActivity
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMainActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMainActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            cvKasir.setOnClickListener {
//                startActivity(Intent(this@AdminMainActivity, AdminJenisProdukActivity::class.java))
            }
            cvJenisProduk.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminJenisProdukActivity::class.java))
            }
            cvProduk.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminProdukActivity::class.java))
            }
            cvKeranjangBelanja.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminKeranjangBelanjaActivity::class.java))
            }
            cvPesanan.setOnClickListener {
//                startActivity(Intent(this@AdminMainActivity, AdminPesananActivity::class.java))
            }
            cvRiwayatPesanan.setOnClickListener {
//                startActivity(Intent(this@AdminMainActivity, AdminRiwayatPesananActivity::class.java))
            }
            cvAkun.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminAkunActivity::class.java))
            }
        }
    }
}