package com.abcd.e_kalontong.ui.activity.user.produk.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.ProdukAdapter
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.ActivitySearchProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogPesanProduk2Binding
import com.abcd.e_kalontong.databinding.AlertDialogPesanProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.ui.activity.admin.kasir.AdminKasirActivity
import com.abcd.e_kalontong.ui.activity.user.main.MainActivity
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchProdukBinding
    @Inject
    lateinit var loading: LoadingAlertDialog
    @Inject
    lateinit var rupiah: KonversiRupiah
    private val viewModel: SearchProdukViewModel by viewModels()
    private var listProduk = ArrayList<ProdukModel>()
    private lateinit var adapter: ProdukAdapter
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferencesLogin()
        setSearchData()
        setButton()
        fetchProduk()
        getProduk()
        getPesan()
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@SearchProdukActivity)
    }

    private fun setSearchData() {
        // Search Data
        binding.srcData.apply {
            requestFocus()
            onActionViewExpanded()
            queryHint = ""
            titleColor = getColor(R.color.white)

            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("MainActivityTAG", "onQueryTextSubmit:1: $query")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("MainActivityTAG", "onQueryTextChange:2: $newText ")
                    adapter.searchData(newText!!)
                    return true
                }

            })
        }
    }

    private fun setButton() {
        binding.btnBack.setOnClickListener {
            if(sharedPreferencesLogin.getIdUser() == 0){
                startActivity(Intent(this@SearchProdukActivity, AdminKasirActivity::class.java))
                finish()
            } else{
                finish()
            }
        }
    }


    private fun fetchProduk() {
        viewModel.fetchProduk()
    }

    private fun getProduk() {
        viewModel.getProduk().observe(this@SearchProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@SearchProdukActivity)
                is UIState.Failure-> setFailureProduk(result.message)
                is UIState.Success-> setSuccessProduk(result.data)
            }
        }
    }

    private fun setFailureProduk(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@SearchProdukActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessProduk(data: ArrayList<ProdukModel>) {
        loading.alertDialogCancel()
//        val sort = data.sortedWith(compareBy { it.nama })
//        val dataArrayList = arrayListOf<ProdukModel>()
//        dataArrayList.addAll(sort)
//        listProduk = dataArrayList
        listProduk = data
        adapter = ProdukAdapter(listProduk, object: OnClickItem.ClickProduk{
            override fun clickItemPesan(produk: ProdukModel, it: View) {
                setShowDialogPesan(produk)
            }
        })
        setRecyclerViewProduk()
    }

    private fun setRecyclerViewProduk() {
        binding.apply {
            rvData.layoutManager = GridLayoutManager(this@SearchProdukActivity, 2)
            rvData.adapter = adapter
        }
    }

    private fun setShowImage(gambar: String, produk: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@SearchProdukActivity)
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

        Glide.with(this@SearchProdukActivity)
            .load("${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.icon_product_home)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setShowDialogPesan(produk: ProdukModel) {
        val view = AlertDialogPesanProduk2Binding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@SearchProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvProduk.text = produk.produk!!
            tvJenisProduk.text = produk.jenis_produk!!.jenis_produk
            tvHarga.text = rupiah.rupiah(produk.harga!!.toLong())

            btnTambah.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if(jumlah < produk.stok!!){
                    jumlah+=1
                    tvJumlah.text = jumlah.toString()
                }
            }
            btnKurang.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if (jumlah > 0){
                    jumlah-=1
                    tvJumlah.text = jumlah.toString()
                }
            }

            Glide.with(this@SearchProdukActivity)
                .load("${Constant.LOCATION_GAMBAR}${produk.gambar}") // URL Gambar
                .placeholder(R.drawable.loading)
                .error(R.drawable.icon_product_home)
                .into(view.ivGambarProduk) // imageView mana yang akan diterapkan

            btnSimpan.setOnClickListener {
                var cek = false
                if(tvJumlah.text.toString().trim() == "0"){
                    Toast.makeText(this@SearchProdukActivity, "Tidak Boleh Bernilai 0", Toast.LENGTH_SHORT).show()
                    cek = true
                }

                if(!cek){
                    dialogInputan.dismiss()
                    val idUser = sharedPreferencesLogin.getIdUser()
                    val idProduk = produk.id_produk!!
                    val jumlah = tvJumlah.text.toString().trim()

                    postPesan(idUser, idProduk, jumlah)
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postPesan(idUser: Int, idProduk: Int, jumlah: String) {
        viewModel.postPesan(idUser, idProduk, jumlah)
    }

    private fun getPesan(){
        viewModel.getPesan().observe(this@SearchProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@SearchProdukActivity)
                is UIState.Success-> setSuccessPostPesan(result.data)
                is UIState.Failure-> setFailurePostPesan(result.message)
            }
        }
    }

    private fun setFailurePostPesan(message: String) {
        Toast.makeText(this@SearchProdukActivity, "Gagal pesan : $message", Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessPostPesan(data: ResponseModel) {
        if(data.status == "0"){
            Toast.makeText(this@SearchProdukActivity, "Berhasil Pesan", Toast.LENGTH_SHORT).show()
//            if(sharedPreferencesLogin.getIdUser() == 0){
//                AdminKasirActivity().fetchPesananKasir()
//            }
        } else{
            Toast.makeText(this@SearchProdukActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(sharedPreferencesLogin.getIdUser() == 0){
            startActivity(Intent(this@SearchProdukActivity, AdminKasirActivity::class.java))
            finish()
        } else{
            finish()
        }
    }
}