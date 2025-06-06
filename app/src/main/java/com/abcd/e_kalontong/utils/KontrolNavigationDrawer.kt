package com.abcd.e_kalontong.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.ui.activity.admin.akun.AdminAkunActivity
import com.abcd.e_kalontong.ui.activity.admin.jenis_produk.AdminJenisProdukActivity
import com.abcd.e_kalontong.ui.activity.admin.kasir.AdminKasirActivity
import com.abcd.e_kalontong.ui.activity.admin.keranjang_belanja.AdminKeranjangBelanjaActivity
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity
import com.abcd.e_kalontong.ui.activity.admin.pesanan.AdminPesananActivity
import com.abcd.e_kalontong.ui.activity.admin.produk.AdminProdukActivity
import com.abcd.e_kalontong.ui.activity.admin.riwayat_pesanan.AdminRiwayatPesananActivity
import com.abcd.e_kalontong.ui.activity.login.LoginActivity

//import com.abcd.e_kalontong.ui.activity.login.LoginActivity

class KontrolNavigationDrawer(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "admin"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        }
    }

    @SuppressLint("ResourceAsColor")
    fun onClickItemNavigationDrawer(navigation: com.google.android.material.navigation.NavigationView, navigationLayout: DrawerLayout, igNavigation:ImageView, activity: Activity){
        navigation.setNavigationItemSelectedListener {
            if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
                    R.id.adminNavDrawerHome -> {
                        val intent = Intent(context, AdminMainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerKasir -> {
                        val intent = Intent(context, AdminKasirActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerJenisProduk -> {
                        val intent = Intent(context, AdminJenisProdukActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerProduk -> {
                        val intent = Intent(context, AdminProdukActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerKeranjangBelanja -> {
                        val intent = Intent(context, AdminKeranjangBelanjaActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerPesanan-> {
                        val intent = Intent(context, AdminPesananActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerRiwayatPesanan -> {
                        val intent = Intent(context, AdminRiwayatPesananActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerAkun -> {
                        val intent = Intent(context, AdminAkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminBtnKeluar ->{
                        logoutAdmin(activity)
                    }
                }
            }
            navigationLayout.setBackgroundColor(R.color.white)
            navigationLayout.closeDrawer(GravityCompat.START)
            true
        }
        // garis 3 navigasi
        igNavigation.setOnClickListener {
            navigationLayout.openDrawer(GravityCompat.START)
        }
    }

    fun logoutAdmin(activity: Activity){
        sharedPreferences.setLogout()
        context.startActivity(Intent(context, LoginActivity::class.java))
        activity.finish()
    }

//    fun logoutAdmin(activity: Activity){
//        sharedPreferences.setLogin(0, "", "","", "","")
//        context.startActivity(Intent(context, LoginActivity::class.java))
//        activity.finish()
//    }
}