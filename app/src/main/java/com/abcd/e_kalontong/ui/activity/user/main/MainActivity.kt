package com.abcd.e_kalontong.ui.activity.user.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.databinding.ActivityMainBinding
import com.abcd.e_kalontong.ui.fragment.user.home.HomeFragment
import com.abcd.e_kalontong.ui.fragment.user.product.ProductFragment
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferencesLogin
    private lateinit var scaleAnimation: ScaleAnimation
    private var checkFragmentPosition = 0   // 0 Home, 1 product, 2 history, 3 account
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurationSharedPreferences()
        setScaleAnimation()
        setFragment(HomeFragment())
        setButtonBottomBar()
    }
    private fun configurationSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@MainActivity)
    }

    private fun setButtonBottomBar() {
        binding.icBottom.apply {
            btnHome.setOnClickListener {
                clickHome()
            }
            btnProduk.setOnClickListener {
                clickProduk()
            }
            btnPesanan.setOnClickListener {
                clickPesanan()
            }
            btnAccount.setOnClickListener {
                clickAccount()
            }
        }
    }

    fun clickHome() {
        binding.icBottom.apply {
            // text color
            tvHome.setTextColor(resources.getColor(R.color.primaryColor))
            tvProduk.setTextColor(resources.getColor(R.color.textColorBlack))
            tvPesanan.setTextColor(resources.getColor(R.color.textColorBlack))
            tvAccount.setTextColor(resources.getColor(R.color.textColorBlack))

            // image view visibility
            ivHome.setImageResource(R.drawable.icon_home_active)
            ivProduk.setImageResource(R.drawable.icon_product)
            ivPesanan.setImageResource(R.drawable.icon_riwayat)
            ivAccount.setImageResource(R.drawable.icon_akun)

            setFragment(HomeFragment())
            checkFragmentPosition = 0
        }
    }

    fun clickProduk() {
        binding.icBottom.apply {
            // text color
            tvHome.setTextColor(resources.getColor(R.color.textColorBlack))
            tvProduk.setTextColor(resources.getColor(R.color.primaryColor))
            tvPesanan.setTextColor(resources.getColor(R.color.textColorBlack))
            tvAccount.setTextColor(resources.getColor(R.color.textColorBlack))

            // image view visibility
            ivHome.setImageResource(R.drawable.icon_home)
            ivProduk.setImageResource(R.drawable.icon_product_active)
            ivPesanan.setImageResource(R.drawable.icon_riwayat)
            ivAccount.setImageResource(R.drawable.icon_akun)

            setFragment(ProductFragment())
            checkFragmentPosition = 1
        }
    }

    fun clickPesanan() {
        binding.icBottom.apply {
            // text color
            tvHome.setTextColor(resources.getColor(R.color.textColorBlack))
            tvProduk.setTextColor(resources.getColor(R.color.textColorBlack))
            tvPesanan.setTextColor(resources.getColor(R.color.primaryColor))
            tvAccount.setTextColor(resources.getColor(R.color.textColorBlack))

            // image view visibility
            ivHome.setImageResource(R.drawable.icon_home)
            ivProduk.setImageResource(R.drawable.icon_product)
            ivPesanan.setImageResource(R.drawable.icon_riwayat_active)
            ivAccount.setImageResource(R.drawable.icon_akun)

//            setFragment(PesananFragment())
            checkFragmentPosition = 2
        }
    }

    fun clickAccount() {
        binding.icBottom.apply {
            // text color
            tvHome.setTextColor(resources.getColor(R.color.textColorBlack))
            tvProduk.setTextColor(resources.getColor(R.color.textColorBlack))
            tvPesanan.setTextColor(resources.getColor(R.color.textColorBlack))
            tvAccount.setTextColor(resources.getColor(R.color.primaryColor))

            // image view visibility
            ivHome.setImageResource(R.drawable.icon_home)
            ivProduk.setImageResource(R.drawable.icon_product)
            ivPesanan.setImageResource(R.drawable.icon_riwayat)
            ivAccount.setImageResource(R.drawable.icon_akun_active)

//            setFragment(AccountFragment())
            checkFragmentPosition = 3
        }
    }

    @SuppressLint("CommitTransaction")
    private fun setFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.flMain, fragment)
            commit()
        }
    }

    private fun setScaleAnimation(){
        scaleAnimation = ScaleAnimation(
            0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        scaleAnimation.apply {
            duration = 200
            fillAfter = true
        }
    }

    private var tapDuaKali = false
    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(checkFragmentPosition == 0){
            if (tapDuaKali){
                super.onBackPressed()
            }
            tapDuaKali = true
            Toast.makeText(this@MainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                tapDuaKali = false
            }, 2000)
        } else{
            clickHome()
        }
    }
}