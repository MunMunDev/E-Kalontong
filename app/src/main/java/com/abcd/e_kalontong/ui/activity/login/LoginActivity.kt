package com.abcd.e_kalontong.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.abcd.e_kalontong.data.model.UserModel
import com.abcd.e_kalontong.databinding.ActivityLoginBinding
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity
import com.abcd.e_kalontong.ui.activity.register.RegisterActivity
import com.abcd.e_kalontong.ui.activity.user.main.MainActivity
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    @Inject
    lateinit var loading : LoadingAlertDialog
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        konfigurationUtils()
        button()
        getUser()
    }

    private fun konfigurationUtils() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@LoginActivity)
    }

    private fun button(){
        loginBinding.apply {
            btnLogin.setOnClickListener {
                btnLogin()
            }
            tvDaftar.setOnClickListener {
                btnDaftar()
            }
        }
    }

    private fun btnDaftar() {
        loginBinding.tvDaftar.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun btnLogin() {
        loginBinding.apply {
            if(etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                loading.alertDialogLoading(this@LoginActivity)
                fetchUsers(etUsername.text.toString(), etPassword.text.toString())
            }
            else{
                if(etUsername.text.isEmpty()){
                    etUsername.error = "Masukkan Username"
                }
                if(etPassword.text.isEmpty()){
                    etPassword.error = "Masukkan Password"
                }
            }
        }
    }

    private fun fetchUsers(username: String, password: String) {
        loginViewModel.fetchUser(username, password)
    }

    private fun getUser(){
        loginViewModel.getUser().observe(this@LoginActivity){result ->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@LoginActivity)
                is UIState.Success-> setSuccessUser(result.data)
                is UIState.Failure -> setFailureUser(result.message)
            }
        }
    }

    private fun setFailureUser(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUser(data: ArrayList<UserModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            successFetchLogin(data)
        } else{
            Toast.makeText(this@LoginActivity, "Data tidak ditemukan \nPastikan Username dan Password ada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun successFetchLogin(userModel: ArrayList<UserModel>){
        var valueIdUser = 0
        userModel[0].idUser?.let {
            valueIdUser = it.toInt()
        }
        val valueNama = userModel[0].nama.toString()
        val valueNomorHp = userModel[0].nomor_hp.toString()
        val valueUsername = userModel[0].username.toString()
        val valuePassword = userModel[0].password.toString()
        val valueSebagai= userModel[0].sebagai.toString()

        try{
            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
            if(valueSebagai=="user"){
                sharedPreferencesLogin.setLogin(valueIdUser, valueNama, valueNomorHp, valueUsername, valuePassword, valueSebagai)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else if(valueSebagai=="admin"){
                sharedPreferencesLogin.setLogin(0, valueNama, valueNomorHp, valueUsername, valuePassword, valueSebagai)
                startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
                finish()
            } else{
                Toast.makeText(this@LoginActivity, "Data tidak ditemukan \nPastikan Username dan Password ada", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: Exception){
            Toast.makeText(this@LoginActivity, "gagal: $ex", Toast.LENGTH_SHORT).show()
        }
    }

    private var tapDuaKali = false
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@LoginActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)
    }
}