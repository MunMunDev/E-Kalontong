package com.abcd.e_kalontong.utils

class KonversiRupiah {

    fun rupiah(angka: Long): String{
        var hasilAwal = ""
        var hasilAkhir = "Rp."
        var arrayAngka = angka.toString().split("")

        var angkaTemp = 1
        for(value in angka.toString().length downTo 1 step 1){
            hasilAwal+=arrayAngka[value]
            if(angkaTemp==3){
                hasilAwal+="."
            }
            if(angkaTemp==6){
                hasilAwal+="."
            }
            if(angkaTemp==9){
                hasilAwal+="."
            }
            if(angkaTemp==12){
                hasilAwal+="."
            }
            if(angkaTemp==15){
                hasilAwal+="."
            }
            angkaTemp++
        }

        arrayAngka = hasilAwal.split("")
        for(value in hasilAwal.length downTo 1 step 1){
            if(hasilAwal.length == value){
                if(arrayAngka[value] != "."){
                    hasilAkhir+=arrayAngka[value]
                }
            }
            else{
                hasilAkhir+=arrayAngka[value]
            }
        }

        return hasilAkhir
    }

}