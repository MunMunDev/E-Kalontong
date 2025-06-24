package com.abcd.e_kalontong.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity
import com.abcd.e_kalontong.ui.activity.login.LoginActivity
import com.abcd.e_kalontong.ui.activity.user.main.MainActivity
import com.abcd.e_kalontong.utils.SharedPreferencesLogin

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferencesLogin = SharedPreferencesLogin(this@SplashScreenActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPreferencesLogin.getIdUser().toString() != ""){
                if(sharedPreferencesLogin.getSebagai() == "user"){
                    // To User
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                } else if(sharedPreferencesLogin.getSebagai() == "admin"){
                    // To admin
                    startActivity(Intent(this@SplashScreenActivity, AdminMainActivity::class.java))
                    finish()
                } else{
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    finish()
                }
            }
            else{
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }
        }, 3000)
    }
}
