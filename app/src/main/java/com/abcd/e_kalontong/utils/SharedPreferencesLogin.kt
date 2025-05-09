package com.abcd.e_kalontong.utils

import android.content.Context

class SharedPreferencesLogin(val context: Context) {
    val keyId = "keyId"
    val keyNis = "keyNis"
    val keyNama = "keyNama"
    val keyKelas = "keyKelas"
    val keyPassword = "keyPassword"
    val keySebagai = "keySebagai"

    // Tambahan Guru
    val keyNip = "keyNip"
    val keyNomorHp = "keyNomorHp"
    val keyMatpel = "keyMatpel"

    var sharedPref = context.getSharedPreferences("sharedpreference_login", Context.MODE_PRIVATE)
    var editPref = sharedPref.edit()

    fun setLoginSiswa(id_user:String, nis:String, nama:String, kelas:String, nomorHp:String, password:String, sebagai:String){
        editPref.apply{
            putString(keyId, id_user)
            putString(keyNis, nis)
            putString(keyNama, nama)
            putString(keyKelas, kelas)
            putString(keyNomorHp, nomorHp)
            putString(keyPassword, password)
            putString(keySebagai, sebagai)
            apply()
        }
    }
    fun setLoginGuru(id_user:String, nip:String, nama:String, nomorHp:String, matpel:String, password:String, sebagai:String){
        editPref.apply{
            putString(keyId, id_user)
            putString(keyNip, nip)
            putString(keyNama, nama)
            putString(keyNomorHp, nomorHp)
            putString(keyMatpel, matpel)
            putString(keyPassword, password)
            putString(keySebagai, sebagai)
            apply()
        }
    }

    fun getId(): String{
        return sharedPref.getString(keyId, "0").toString()
    }
    fun getNis():String{
        return sharedPref.getString(keyNis, "").toString()
    }
    fun getNama():String{
        return sharedPref.getString(keyNama, "").toString()
    }
    fun getKelas():String{
        return sharedPref.getString(keyKelas, "").toString()
    }
    fun getNomorHp():String{
        return sharedPref.getString(keyNomorHp, "").toString()
    }
    fun getPassword(): String{
        return sharedPref.getString(keyPassword, "").toString()
    }
    fun getSebagai(): String{
        return sharedPref.getString(keySebagai, "").toString()
    }

    // Tambahan Guru
    fun getNip(): String{
        return sharedPref.getString(keyNip, "").toString()
    }
    fun getMatpel(): String{
        return sharedPref.getString(keyMatpel, "").toString()
    }
}