package com.abcd.e_kalontong.utils

class KataAcak {
    fun getHurufSaja(): String{
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var hurufAcak = "1"
        for(i in 1..20){
            hurufAcak+=str.random()
        }
        return hurufAcak
    }
    fun getHurufDanAngka(): String{
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var hurufAcak = "1"
        for(i in 1..20){
            hurufAcak+=str.random()
        }
        return hurufAcak
    }
}