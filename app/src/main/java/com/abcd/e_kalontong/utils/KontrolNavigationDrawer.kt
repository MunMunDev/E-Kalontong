package com.abcd.e_kalontong.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.ui.activity.admin.jenis_produk.AdminJenisProdukActivity
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity

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

                    }
                    R.id.adminNavDrawerJenisProduk -> {
                        val intent = Intent(context, AdminJenisProdukActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerProduk -> {

                    }
                    R.id.adminNavDrawerKeranjangBelanja -> {

                    }
                    R.id.adminNavDrawerPesanan-> {

                    }
                    R.id.adminNavDrawerRiwayatPesanan -> {

                    }
                    R.id.adminNavDrawerAkun -> {

                    }
                    R.id.adminDrawerBottom ->{
                        logoutGuruAdmin(activity)
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

    fun logoutGuruAdmin(activity: Activity){
//        sharedPreferences.setLoginGuru("", "", "","", "","", "")
//        context.startActivity(Intent(context, LoginActivity::class.java))
//        activity.finish()
    }

//    fun logoutAdmin(activity: Activity){
//        sharedPreferences.setLogin(0, "", "","", "","")
//        context.startActivity(Intent(context, LoginActivity::class.java))
//        activity.finish()
//    }
}